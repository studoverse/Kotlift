package com.moshbit.kotlift

import java.io.File
import java.util.*

fun listFiles(directoryName: String, files: ArrayList<File>, extension: String = "") {
  val directory = File(directoryName)

  // get all the files from a directory
  val fList = directory.listFiles()

  if (fList != null) {
    for (file in fList) {
      if (file.isFile) {
        if (file.name.endsWith(extension)) {
          files.add(file)
        }
      } else if (file.isDirectory) {
        listFiles(file.absolutePath, files, extension)
      }
    }
  }
}