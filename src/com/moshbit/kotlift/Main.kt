package com.moshbit.kotlift

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.*

data class Replacement(val from: String, val to: String)

fun main(args: Array<String>) {
  if (args.count() < 3 || args.count() > 4) {
    println("Usage: kotlift src/kotlin dest/swift replacementFile.json")
    println("or:    kotlift src/kotlin dest/swift replacementFile.json dest/test/swift")
    println("calling with a test path validates all files")
    return;
  }

  val sourcePath = args[0];
  val destinationPath = args[1];

  // Replacement array
  val mapper = jacksonObjectMapper()
  mapper.enable(JsonParser.Feature.ALLOW_COMMENTS)
  val replacements: List<Replacement> = mapper.readValue(Paths.get(args[2]).toFile())
  println(replacements)

  val sourceFiles = ArrayList<File>()
  val destinationFiles = ArrayList<File>()
  listFiles(sourcePath, sourceFiles, ".kt")

  println(sourceFiles)

  for (file in sourceFiles) {
    val lines = Files.readAllLines(Paths.get(file.path), Charsets.UTF_8)
    val destLines = Transpiler(lines, replacements).transpile()

    val destPath = Paths.get(file.path.replace(sourcePath, destinationPath).replace(".kt", ".swift"))
    destinationFiles.add(destPath.toFile())

    Files.write(destPath, destLines, Charsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
  }

  // Validate
  if (args.count() == 4) {
    var errorCount = 0
    val testDestinationPath = args[3];

    val testDestFiles = ArrayList<File>()
    listFiles(testDestinationPath, testDestFiles, ".swift")

    if (destinationFiles.count() != testDestFiles.count()) {
      println("ERROR: INVALID TEST FOLDER:")
      println(destinationFiles)
      println(testDestFiles)
      return;
    }

    // Validate each file
    for (i in 0..sourceFiles.count() - 1) {
      val linesDest = Files.readAllLines(Paths.get(destinationFiles[i].path), Charsets.UTF_8)
      val linesTest = Files.readAllLines(Paths.get(testDestFiles[i].path), Charsets.UTF_8)

      // Check for same line count
      if (linesDest.count() != linesTest.count()) {
        errorCount++
        validateError(destinationFiles[i].path,
            "Invalid line count: dest=${linesDest.count()} test=${linesTest.count()}")
      } else {
        // Compare each line
        for (j in 0..linesDest.count() - 1) {
          if (linesDest[j] != linesTest[j]) {
            errorCount++
            validateError(destinationFiles[i].path, "\n  \"${linesDest[j]}\"\n  \"${linesTest[j]}\"")
          }
        }
      }
    }

    if (errorCount == 0) {
      println("OK")
    } else {
      println("$errorCount ERRORS")
    }
  }
}

fun validateError(fileName: String, hint: String) {
  println("$fileName: $hint")
}
