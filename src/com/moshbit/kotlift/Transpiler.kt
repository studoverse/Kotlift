package com.moshbit.kotlift

import java.util.*
import kotlin.text.Regex

class Transpiler(val source: List<String>, val replacements: List<Replacement>) {
  val dest = ArrayList<String>(source.size)

  // Kotlin has got a main function, but swift playground don't. So add a call to main() on the end.
  var hasMain = false

  val structureTree = ArrayList<StructureTree>()
  var nextLine: String? = null
  var nextInitBlockLine: String = "" // Line inside init {} block

  fun transpile(): List<String> {
    var multiLineComment = false
    var insideTryCatch = false
    var nextLineForReplacement: String? = null
    var nextConstructor: String? = null
    var simulatedNextSourceLine: String? = null

    var i = -1;
    while (i < source.size - 1) {
      // Either get next line from source or from simulatedNextSourceLine
      var line =
          if (simulatedNextSourceLine == null) {
            i++
            source[i].trimEnd()
          } else {
            simulatedNextSourceLine
          }
      simulatedNextSourceLine = null


      // Replace with SWIFT rewrite (keep indent and comment)
      if (nextLineForReplacement != null) {
        dest.add(line.replace(Regex("(\\s*)(.*)"), "$1") + nextLineForReplacement)
        nextLineForReplacement = null
        continue
      }

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


      // Make sure all classes end with {}
      if (line.matches(Regex("\\s*(open |data |)class .*")) && !line.endsWith("{")) {
        line += " {"
        simulatedNextSourceLine = "}"
      }


      // Build structure tree
      var nextStructureTreeElement =
          if (line.matches(Regex("\\s*(open |override |)fun .*"))) {
            Function()
          } else if (line.matches(Regex("(\\s*)(open |data |)class ([A-Za-z0-9_]+)(<.*>|)(\\([^\\)]*\\)|)( ?(.*)) \\{"))) {
            Class()
          } else {
            Block()
          }

      // Match each block start and end, but avoid looking inside strings
      val noStringLine = line.replace(Regex("[^\\\\]\".*[^\\\\]\""), "")
      for (c in noStringLine) {
        if (c == '{') {
          structureTree.add(nextStructureTreeElement)
          nextStructureTreeElement = Block()
        } else if (c == '}') {
          val lastElement = structureTree.last()
          if (lastElement is Class && lastElement.constructorWritten == false && nextConstructor != "") {
            // Apply nextConstructor even when no init{} section was given in kotlin
            dest.add("  init$nextConstructor {$nextInitBlockLine\n  }")
          }

          structureTree.removeAt(structureTree.count() - 1)
        }
      }


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
      // Translate "else ->" -> "default:"
      line = line.replace(Regex("(\\s*)else -> (.*)"), "$1default: $2")
      // Translate "x ->" -> "case x:"
      line = line.replace(Regex("(\\s*)(.*) -> (.*)"), "$1case $2: $3")


      // Translate function calls
      if (line.matches(Regex("\\s*(open |override |)fun .*"))) {
        // fun -> func
        line = line.replace("open fun ", "func ").replace("fun ", "func ")
        // Return values
        line = line.replace("):", ") ->")
        line = line.replace(") :", ") ->")
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


      // Replacments
      replacements.forEach { line = line.replace(it.from, it.to) }


      // Remove package
      if (line.startsWith("package ")) {
        line = ""
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


      // Classes
      // Declaration
      if (line.matches(Regex("(\\s*)(open |data |)class ([A-Za-z0-9_]+)(<.*>|)(\\([^\\)]*\\)|)( ?(.*)) \\{"))) {
        nextConstructor = line.replace(Regex("(\\s*)(open |data |)class ([A-Za-z0-9_]+)(<.*>|)(\\([^\\)]*\\)|)( ?(.*)) \\{"), "$5")

        // Data classes
        val dataClassName =
            if (line.trim().startsWith("data")) {
              line = line.substring(0, line.length - 2) + ": CustomStringConvertible {"
              line.replace(Regex("(\\s*)(open |data |)class ([A-Za-z0-9_]+)(<.*>|)(\\([^\\)]*\\)|)( ?(.*)) \\{"), "$3")
            } else {
              null
            }

        // Constructor parameters
        if (nextConstructor.isEmpty()) {
          nextConstructor = "()"
        } else {
          parseConstructorParams(nextConstructor, dataClassName)
          nextConstructor = nextConstructor.replace("let ", "").replace("var ", "")
        }


        // Inheritance
        val derivedClass = line.replace(Regex("(\\s*)(open |data |)class ([A-Za-z0-9_]+)(<.*>|)(\\([^\\)]*\\)|)( ?(.*)) \\{"), "$7").replace("()", "")
        line = line.replace(Regex("(\\s*)(open |data |)class ([A-Za-z0-9_]+)(<.*>|)(\\([^\\)]*\\)|)( ?(.*)) \\{"), "$1class $3$4$derivedClass {")


        if (!derivedClass.isEmpty()) {
          // Current class is a derived class
          (structureTree.last { it is Class } as Class).derivedClass = true
        }

      }
      // Constructor
      if (line.matches(Regex("(\\s*)init \\{")) || line.matches(Regex("(\\s*)init \\{.*}"))) {
        // TODO @V Decide if all arguments must be labeled or should be unlabeled (external name = "_")
        val prefix = if ((structureTree.last { it is Class } as Class).derivedClass) "override " else ""

        line = line.replace(Regex("(\\s*)init \\{"), "$1${prefix}init$nextConstructor {$nextInitBlockLine")
        nextConstructor = ""

        // Class constructor has been written
        (structureTree.last { it is Class } as Class).constructorWritten = true
      }


      // Named arguments: name = x --> name: x
      while (line.matches(Regex("(.*[A-Z][A-Za-z0-9_]*)(<.*>|)\\((.*?)([A-Za-z0-9_]+) = (.*)\\)(.*)"))) {
        line = line.replace(Regex("(.*[A-Z][A-Za-z0-9_]*)(<.*>|)\\((.*?)([A-Za-z0-9_]+) = (.*)\\)(.*)"), "$1$2($3$4: $5)$6")
      }


      dest.add(line)

      if (nextLine != null) {
        dest.add(nextLine!!)
        nextLine = null
      }
    }

    if (hasMain) {
      dest.add("")
      dest.add("main([])")
    }

    if (structureTree.count() != 0) {
      throw IllegalStateException("Structure tree should be empty, but is $structureTree")
    }

    return dest
  }

  // Constructor might look like "(let name: String, let admin: Bool = false)"
  private fun parseConstructorParams(originalConstructor: String, dataClassName: String?) {
    val parameters = originalConstructor
        .substring(originalConstructor.indexOf('(') + 1, originalConstructor.indexOf(')'))
        .split(",").map { it.trim() }

    val parameterNames = ArrayList<String>()

    // Costructor parameters
    for (parameter in parameters) {
      if (parameter.startsWith("let ") || parameter.startsWith("var ")) {
        // Create field in class
        if (nextLine == null) {
          nextLine = ""
          nextInitBlockLine = ""
        }

        var trimmedParameter =
            if (parameter.contains('=')) {
              parameter.substring(0, parameter.indexOf('=') - 1)
            } else {
              parameter
            }

        nextLine += "  $trimmedParameter\n"
        var parameterName = trimmedParameter.split(' ')[1].removeSuffix(":")
        parameterNames += parameterName
        nextInitBlockLine += "\n    self.$parameterName = $parameterName"
      }
    }

    // Data classes
    if (dataClassName != null) {
      nextLine += parameterNames.joinToString(
          prefix = "\n  var description: String {\n    return \"$dataClassName(",
          postfix = ")\"\n  }\n",
          transform = { "$it=\\($it)" } )
    }
  }

}

open class StructureTree

class Class(var constructorWritten: Boolean = false, var derivedClass: Boolean = false) : StructureTree()
class Function : StructureTree()
class Block : StructureTree()
