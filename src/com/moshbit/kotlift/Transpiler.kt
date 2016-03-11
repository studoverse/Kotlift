package com.moshbit.kotlift

import java.util.*

data class Classes(val name: String, var functions: LinkedList<String>, var properties: LinkedList<String>)
data class Interfaces(val name: String, var functions: LinkedList<String>, var properties: LinkedList<String>)

class Transpiler(val replacements: List<Replacement>) {
  // List of all classes and interfaces. Must be set before transpile() calls.
  // Needed to detect if override should be called or not.
  val classesList = LinkedList<Classes>()
  val interfacesList = LinkedList<Interfaces>()


  fun parse(source: List<String>) {
    // Just transpile each file to parse all the lists
    transpile(source)
  }


  fun transpile(source: List<String>): List<String> {
    // Kotlin has got a main function, but swift playground don't. So add a call to main() on the end.
    var hasMain = false

    val dest = ArrayList<String>(source.size)
    val structureTree = ArrayList<StructureTree>()

    var nextOutputLine: String? = null
    var nextInitBlockLine: String = "" // Line inside init {} block

    var multiLineComment = false
    var insideTryCatch = false
    var nextLineForReplacement: String? = null
    var nextConstructor: String? = null
    var simulatedNextSourceLine: String? = null

    // Constructor might look like "(let name: String, let admin: Bool = false)"
    fun parseConstructorParams(originalConstructor: String, dataClassName: String?) {
      val parameters = originalConstructor
          .substring(originalConstructor.indexOf('(') + 1, originalConstructor.indexOf(')'))
          .split(",").map { it.trim() }

      val parameterNames = ArrayList<String>()

      // Constructor parameters
      for (parameter in parameters) {
        if (parameter.matches(Regex("(.* |)let .*")) || parameter.matches(Regex("(.* |)var .*"))) {
          // Create field in class
          if (nextOutputLine == null) {
            nextOutputLine = ""
            nextInitBlockLine = ""
          }

          var trimmedParameter =
              if (parameter.contains('=')) {
                parameter.substring(0, parameter.indexOf('=') - 1)
              } else {
                parameter
              }

          nextOutputLine += "  $trimmedParameter\n"
          var parameterName = trimmedParameter.split(' ')[1].removeSuffix(":")
          parameterNames += parameterName
          nextInitBlockLine += "\n    self.$parameterName = $parameterName"

          // Append to classes/interface list
          for (index in structureTree.count() - 1 downTo 0) {
            if (structureTree[index] is Class) {
              classesList.last.properties.add(parameterName)
              break
            } else if (structureTree[index] is Interface) {
              interfacesList.last.properties.add(parameterName)
              break
            }
          }
        }
      }

      // Data classes
      if (dataClassName != null) {
        nextOutputLine += parameterNames.joinToString(
            prefix = "\n  var description: String {\n    return \"$dataClassName(",
            postfix = ")\"\n  }\n",
            transform = { "$it=\\($it)" })
      }
    }

    var i = -1
    lineLoop@ while (i < source.size - 1) {
      // Either get next line from source or from simulatedNextSourceLine
      var line =
          if (simulatedNextSourceLine == null) {
            i++
            source[i].trimEnd()
          } else {
            simulatedNextSourceLine
          }
      simulatedNextSourceLine = null
      val nextInputLine = if (i < source.size - 1) source[i + 1] else ""


      // Detect SWIFT rewrite
      if (line.matches(Regex("(.*)SWIFT: (.*)"))) {
        nextLineForReplacement = line.replace(Regex("(.*)SWIFT: (.*)"), "$2")
        continue
      }

      // Detect main function
      if (line.matches(Regex("\\s*fun main\\([A-Za-z0-9_]+: Array<String>\\) \\{"))) {
        hasMain = true
      }


      // Comments lines are only detected when no additional code is in the line
      // Start multi line comments
      if (line.matches(Regex("\\s*/\\*.*"))) {
        multiLineComment = true
      }

      // End multi line comments
      if (line.matches(Regex(".*\\*/\\s*"))) {
        multiLineComment = false
        dest.add(line)
        continue
      }

      // Don't inspect comment lines any further
      if (multiLineComment || line.matches(Regex("\\s*//"))) {
        dest.add(line)
        continue
      }


      //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      // Build structure tree

      // Make sure all classes end with {}
      if (line.matches(Regex("(.* |)class .*")) && !line.endsWith("{")) {
        line += " {"
        simulatedNextSourceLine = "}"
      }

      // Build structure tree
      var nextStructureTreeElement =
          if (line.matches(R_FUN)) {
            Function()
          } else if (line.matches(R_CLASS)) {
            val name = line.replace(R_CLASS, "$4")

            // Add to classes list
            if (classesList.find { it.name == name } == null) {
              classesList.add(Classes(name, LinkedList(), LinkedList()))
            }

            Class()
          } else if (line.matches(Regex("(\\s*)companion object \\{"))) {
            structureTree.add(CompanionObject())
            continue
          } else if (line.matches(Regex("(\\s*)interface ([A-Za-z0-9_]+) \\{"))) {
            val name = line.replace(Regex("(\\s*)interface ([A-Za-z0-9_]+) \\{"), "$2")

            // Add to interfaces list
            if (interfacesList.find { it.name == name } == null) {
              interfacesList.add(Interfaces(name, LinkedList(), LinkedList()))
            }

            Interface(name)
          } else if (line.matches(Regex("(\\s*)when (.*)"))) {
            SwitchCase()
          } else {
            Block()
          }

      // Match each block start and end, but avoid looking inside strings
      var noStringLine = line
      while (noStringLine.matches(Regex("(.*[^\\\\])\"(.+?[^\\\\])\"(.*)"))) {
        noStringLine = noStringLine.replace(Regex("(.*[^\\\\])\"(.+?[^\\\\])\"(.*)"), "$1\"\"$3")
      }

      for (c in noStringLine) {
        if (c == '{') {
          structureTree.add(nextStructureTreeElement)
          nextStructureTreeElement = Block()
        } else if (c == '}') {
          val lastElement = structureTree.last()

          // Apply nextConstructor even when no init{} section was given in kotlin
          if (lastElement is Class && lastElement.constructorWritten == false && nextConstructor != "") {
            val prefix = if (lastElement.parentClass != null) "override " else ""
            dest.add("  $prefix$nextConstructor {$nextInitBlockLine\n  }")
          }

          // Ignore closing brackets from companion objects
          if (lastElement is CompanionObject ) {
            structureTree.removeAt(structureTree.lastIndex)
            continue@lineLoop
          }

          structureTree.removeAt(structureTree.lastIndex)

          // Add line if noted in structure tree
          if (structureTree.isNotEmpty() && structureTree.last() is AddLine) {
            val addLine = (structureTree.last() as AddLine)
            if (addLine.nextLine) {
              // Don't close bracket when setter of computed property comes next
              if (addLine is ComputedProperty && nextInputLine.matches(Regex("\\s*(get|set)\\(.*"))) {
                structureTree.add(ComputedProperty())
              } else {
                nextOutputLine = addLine.lineToInsert
              }
            } else {
              dest.add(addLine.lineToInsert)
            }
            structureTree.removeAt(structureTree.lastIndex)
          }
        }
      }


      // Replace with SWIFT rewrite (keep indent and comment)
      if (nextLineForReplacement != null) {
        dest.add(line.replace(Regex("(\\s*)(.*)"), "$1") + nextLineForReplacement)
        nextLineForReplacement = null
        continue
      }

      //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      // Start with translations

      // Translate arrays
      while (line.matches(Regex("(.*)Array<([A-Za-z0-9_]+)>(.*)"))) {
        line = line.replace(Regex("(.*)Array<([A-Za-z0-9_]+)>(.*)"), "$1[$2]$3")
      }


      // Translate range .. --> ...
      if (line.matches(Regex("(.*)\\.\\.(.*)"))) {
        line = line.replace(Regex("(.*)\\.\\.(.*)"), "$1...$2")
      }

      // Translate if in range if x in 1..7 --> if case 1...7 = x
      if (line.matches(Regex("(.*)if \\((.*) in (.*)\\.\\.\\.(.*)\\)(.*)"))) {
        line = line.replace(Regex("(.*)if \\((.*) in (.*)\\.\\.\\.(.*)\\)(.*)"), "$1if case $3...$4 = $2$5")
      }


      // Switch
      // Translate when -> switch
      line = line.replace(Regex("(\\s*)when (.*)"), "$1switch $2")

      if (structureTree.isNotEmpty() && structureTree.last() is SwitchCase) {
        // Translate "else ->" -> "default:"
        line = line.replace(Regex("(\\s*)else -> (.*)"), "$1default: $2")
        // Translate "x ->" -> "case x:"
        line = line.replace(Regex("(\\s*)(.*) -> (.*)"), "$1case $2: $3")
      }


      // Translate function calls
      if (line.matches(R_FUN)) {
        val functionName = line.replace(R_FUN, "$6")

        // Append to classes/interface list
        for (index in structureTree.count() - 1 downTo 0) {
          if (structureTree[index] is Class) {
            classesList.last.functions.add(functionName)
            break
          } else if (structureTree[index] is Interface) {
            interfacesList.last.functions.add(functionName)
            break
          }
        }

        // Generic: fun <T> foo() --> func foo<T>()
        line = line.replace(R_FUN, "$1$2func $6$5$7")

        // fun -> func
        line = line.replace("open ", "")
        // Return values
        line = line.replace("):", ") ->")
        line = line.replace(") :", ") ->")

        // No swift override-keyword when function is defined by interface
        for (index in structureTree.count() - 1 downTo 0) {
          val structureTreeNode = structureTree[index]
          if (structureTreeNode is Class) {
            // Check all parent interfaces
            for (parentInterface in structureTreeNode.parentInterfaces) {
              if (interfacesList.find { it.name == parentInterface }?.functions?.find { it == functionName } != null) {
                // Function found in interface: Remove "override"
                line = line.replace("override ", "")
              }
            }
            break
          }
        }

        // Abstract
        if (line.contains("abstract ")) {
          line = line.replace("abstract ", "") + " {\n    fatalError(\"Method is abstract\")\n  }"
        }
      }


      // Properties
      if (line.matches(Regex("\\s*(var|val) ([A-Za-z0-9_]+).*"))) {
        // Append to classes/interface list
        for (index in structureTree.count() - 1 downTo 0) {
          if (structureTree[index] is Class) {
            classesList.last.properties.add(line.replace(Regex("\\s*(var|val) ([A-Za-z0-9_]+).*"), "$2"))
            break
          } else if (structureTree[index] is Interface) {
            interfacesList.last.properties.add(line.replace(Regex("\\s*(var|val) ([A-Za-z0-9_]+).*"), "$2"))
            break
          }
        }

        // Custom getter+setter / computed properties
        if (nextInputLine.matches(Regex("\\s*(get|set)\\(.*"))) {
          // In swift computed properties must always be var
          line = line.replace("val ", "var ")
          line += " {"

          // Add closing bracket
          structureTree.add(ComputedProperty())
        }
      }


      // Translate elvis operator if (a) b else c --> a ? b : c
      while (line.matches(Regex("(\\s*)(.*)if \\((.*)\\) (.*) else (.*)"))) {
        line = line.replace(Regex("(\\s*)(.*)if \\((.*)\\) (.*) else (.*)"), "$1$2$3 ? $4 : $5")
      }

      // Translate if (no round brackets in swift)
      line = line.replace(Regex("(\\s*)(if|for|switch|while) \\((.*)\\)(.*)"), "$1$2 $3$4")


      // Translate string interpolation: ${value}
      while (line.matches(Regex("(.*)\\\$\\{(.*)\\}(.*)"))) {
        line = line.replace(Regex("(.*)\\\$\\{(.*)\\}(.*)"), "$1\\\\($2)$3")
      }
      // Translate string interpolation: $value
      while (line.matches(Regex("(.*)\\\$([A-Za-z0-9_]*)([^A-Za-z0-9_].*)"))) {
        line = line.replace(Regex("(.*)\\\$([A-Za-z0-9_]*)([^A-Za-z0-9_].*)"), "$1\\\\($2)$3")
      }


      // Replacements
      replacements.forEach {
        val regex = Regex("(.*[^A-Za-z0-9_]|)${it.from}([^A-Za-z0-9_].*|\\Z)")
        while (line.matches(regex)) {
          line = line.replace(regex, "$1${it.to}$2")
        }
      }


      // Remove .toString()
      line = line.replace(".toString()", "")

      // Math.abs --> abs (same for all other functions in Math package)
      line = line.replace("Math.", "")

      // Float --> Double
      while (line.matches(Regex("(.*[^A-Za-z0-9_])([0-9]+)(\\.[0-9]*)?f([^A-Za-z0-9_].*|\\Z)"))) {
        line = line.replace(Regex("(.*[^A-Za-z0-9_])([0-9]+)(\\.[0-9]*)?f([^A-Za-z0-9_].*|\\Z)"), "$1$2$3$4")
      }

      // 123L --> 123
      while (line.matches(Regex("(.*[^A-Za-z0-9_])([0-9]+)L([^A-Za-z0-9_].*|\\Z)"))) {
        line = line.replace(Regex("(.*[^A-Za-z0-9_])([0-9]+)L([^A-Za-z0-9_].*|\\Z)"), "$1$2$3")
      }

      // Remove package
      if (line.startsWith("package ") || line.startsWith("import ")) {
        continue
      }


      // Translate catch. Exception is stripped out, add type when needed
      if (line.matches(Regex("([\\s\\}]*)catch (.*) \\{"))) {
        line = line.replace(Regex("([\\s\\}]*)catch (.*) \\{"), "$1catch {")
        insideTryCatch = false
      }

      // Append try to each line inside try-catch
      if (insideTryCatch && line.trim().length > 0) {
        if (line.contains("return ")) {
          // return x() --> return try x()
          line = line.replace("return ", "return try ")
        } else if (line.contains(" = ")) {
          // var x = a() --> var x = let a()
          line = line.replace(" = ", " = try ")

        } else {
          // x() --> try x()
          line = line.replace(Regex("(\\s*)(.*)"), "$1try $2")
        }
      }

      // Translate try --> do
      if (line.endsWith("try {")) {
        insideTryCatch = true
        line = line.replace("try", "do")
      }


      // Translate arrayListOf -> []
      if (line.matches(Regex("(.*)arrayListOf<(.*)>(.*)"))) {
        line = line.replace(Regex("(.*)arrayListOf<(.*)>(.*)"), "$1[$2]$3")
      }
      if (line.matches(Regex("(.*)arrayListOf?\\((.*)\\)"))) {
        line = line.replace(Regex("(.*)arrayListOf\\((.*)\\)"), "$1[$2]")
      }

      // Translate List, ArrayList and LinkedList --> Array
      if (line.matches(Regex("(.*)ArrayList<(.*)>(.*)"))) {
        line = line.replace(Regex("(.*)ArrayList<(.*)>(.*)"), "$1[$2]$3")
      }
      if (line.matches(Regex("(.*)LinkedList<(.*)>(.*)"))) {
        line = line.replace(Regex("(.*)LinkedList<(.*)>(.*)"), "$1[$2]$3")
      }
      if (line.matches(Regex("(.*)List<(.*)>(.*)"))) {
        line = line.replace(Regex("(.*)List<(.*)>(.*)"), "$1Array<$2>$3")
      }


      // Classes
      // Declaration
      if (line.matches(R_CLASS)) {
        // Classes (and its members) are in kotlin default public when not otherwise specified
        val modifier = line.replace(R_CLASS, "$2")
        if (!modifier.contains("private ") && !modifier.contains("protected ")
            && !modifier.contains("internal ") && !modifier.contains("public ")) {
          line = "public " + line
        }

        nextConstructor = line.replace(R_CLASS, "init$6")

        // Data classes
        val dataClassName =
            if (line.matches(Regex("(.* |)data .*"))) {
              line = line.substring(0, line.length - 2) + ": CustomStringConvertible {"
              line.replace(R_CLASS, "$4")
            } else {
              null
            }

        // Constructor parameters
        if (!nextConstructor.contains("(")) {
          nextConstructor = "init()"
        } else {
          parseConstructorParams(nextConstructor, dataClassName)
          nextConstructor = nextConstructor.replace("let ", "").replace("var ", "")
        }


        // Inheritance
        val derivedClasses = line.replace(R_CLASS, "$8")
        line = line.replace(R_CLASS, "$1$2class $4$5${derivedClasses.replace("()", "")} {")
            .replace("open ", "").replace("abstract ", "").replace("data ", "")

        if (derivedClasses.startsWith(":")) {
          // Add parent class and interfaces to structure tree
          for (derivedClass in derivedClasses.substring(1).split(", ")) {
            if (derivedClass.trim().endsWith("()")) {
              // Class
              (structureTree.last { it is Class } as Class).parentClass = derivedClass.replace("()", "").trim()
            } else {
              // Interface
              (structureTree.last { it is Class } as Class).parentInterfaces.add(derivedClass.replace("()", "").trim())
            }
          }
        }

      }
      // Constructor
      if (line.matches(Regex("(\\s*)(override |)init \\{")) || line.matches(Regex("(\\s*)(override |)init \\{.*}"))) {
        // TODO @V Decide if all arguments must be labeled or should be unlabeled (external name = "_")
        val prefix = if ((structureTree.last { it is Class } as Class).parentClass != null) "override " else ""

        line = line.replace(Regex("(\\s*)(override |)init \\{"), "$1$prefix$nextConstructor {$nextInitBlockLine")
        nextConstructor = ""

        // Class constructor has been written
        (structureTree.last { it is Class } as Class).constructorWritten = true
      }


      // Named arguments: name = x --> name: x
      while (line.matches(Regex("(.*[A-Z][A-Za-z0-9_]*)(<.*>|)\\((.*?)([A-Za-z0-9_]+) = (.*)\\)(.*)"))) {
        line = line.replace(Regex("(.*[A-Z][A-Za-z0-9_]*)(<.*>|)\\((.*?)([A-Za-z0-9_]+) = (.*)\\)(.*)"), "$1$2($3$4: $5)$6")
      }


      // Arrays should be always mutable, as mutation of elements changes array (in contrast to Kotlin lists)
      if (line.matches(Regex("(\\s*)let (.* = \\[.*\\]\\(\\))"))) {
        line = line.replace((Regex("(\\s*)let (.* = \\[.*\\]\\(\\))")), "$1var $2")
      }


      // Extension functions
      if (line.matches(Regex("(\\s*)func Array(<.*>|)\\.(.*)"))) {
        // List -> Array

        // Int and Double can not be extended when used in an array, so use Addable protocol
        var listType = line.replace(Regex("(\\s*)func Array(<.*>|)\\.(.*)"), "$2")
        if (listType == "<Int>" || listType == "<Double>") {
          listType = "Addable"
        }
        listType = listType.replace("<", "").replace(">", "")

        line = line.replace(Regex("(\\s*)func Array(<.*>|)\\.(.*)"), "extension Array where Element : $listType {\n") +
            line.replace(Regex("(\\s*)func ([A-Za-z0-9_]+)(<.*>|)\\.(.*)"), "$1func $4")

        // Close extension
        structureTree.add(structureTree.size - 1, AddLine("}"))

      } else if (line.matches(Regex("(\\s*)func ([A-Za-z0-9_]+)(<.*>|)\\.(.*)"))) {
        // All others
        line = line.replace(Regex("(\\s*)func ([A-Za-z0-9_]+)(<.*>|)\\.(.*)"), "extension $1$2$3 {\n") +
            line.replace(Regex("(\\s*)func ([A-Za-z0-9_]+)(<.*>|)\\.(.*)"), "$1func $4")

        // Close extension
        structureTree.add(structureTree.size - 1, AddLine("}"))
      }


      // Companion object / static
      if (line.matches(Regex("(\\s*)(func|var|let) (.*)")) && !structureTree.none { it is CompanionObject }) {
        line = line.replace(Regex("(\\s*)(func|var|let) (.*)"), "$1static $2 $3")
      }


      // Lambdas: { x -> x } --> { x in x }
      while (line.matches(Regex("(.*\\{.*) -> (.*\\}.*)"))) {
        line = line.replace(Regex("(.*\\{.*) -> (.*\\}.*)"), "$1 in $2")
      }
      // Lambdas: { it } --> { $0 }
      while (line.matches(Regex("(.*\\{.*)it(.*\\}.*)"))) {
        line = line.replace(Regex("(.*\\{.*)it(.*\\}.*)"), "$1\\$0$2")
      }


      // Force optional unwrapping: !! -> !
      line = line.replace("!!", "!")


      // Null coalescing: ?: --> ??
      line = line.replace(" ?: ", " ?? ")

      // Return null on elvis operator
      if (line.matches(Regex("(\\s*)(.* )([A-Za-z0-9_]*) = (.*) \\?\\? return nil"))) {
        val indent = line.replace(Regex("(\\s*)(.* )([A-Za-z0-9_]*) = (.*) \\?\\? return nil"), "$1")
        val nullVar = line.replace(Regex("(\\s*)(.* )([A-Za-z0-9_]*) = (.*) \\?\\? return nil"), "$3")
        line = line.replace(Regex("(\\s*)(.* )([A-Za-z0-9_]*) = (.*) \\?\\? return nil"), "$1$2$3 = $4 ?? nil")
        // Add return statement
        nextOutputLine = "${indent}if $nullVar == nil { return nil } // Return from elvis operator"
      }


      dest.add(line)

      if (nextOutputLine != null) {
        dest.add(nextOutputLine!!)
        nextOutputLine = null
      }
    }

    if (hasMain) {
      dest.add("")
      dest.add("main([])")
    }

    if (structureTree.count() != 0) {
      throw IllegalStateException("Structure tree should be empty, but is $structureTree")
    }

    // Print classes and interfaces
    if (DEBUG) {
      for (classes in classesList) {
        println("        class ${classes.name}: ${classes.functions} ${classes.properties}")
      }
      for (interfaces in interfacesList) {
        println("        interface ${interfaces.name}: ${interfaces.functions} ${interfaces.properties}")
      }
    }

    return dest
  }

  companion object {
    val DEBUG = false

    val R_CLASS = Regex("(\\s*)((open |data |abstract |private |protected |internal |public |)*)class ([A-Za-z0-9_]+)(<.+?>|)(\\([^\\)]*\\)|)( ?(.*)) \\{")
    val R_FUN = Regex("(\\s*)((open |override |abstract |private |protected |internal |public |)*)fun( |(<.+?>))+([A-Za-z0-9_<>.]+)(\\(.*\\).*)")
  }

}

open class StructureTree

class Class(var constructorWritten: Boolean = false, var parentClass: String? = null, var parentInterfaces: LinkedList<String> = LinkedList()) : StructureTree()
class Function : StructureTree()
class Block : StructureTree()
class SwitchCase : StructureTree()
open class AddLine(val lineToInsert: String, val nextLine: Boolean = false) : StructureTree()
class ComputedProperty : AddLine("}", nextLine = true)
class CompanionObject : StructureTree()
class Interface(name: String) : StructureTree()