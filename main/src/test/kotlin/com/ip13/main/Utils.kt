package com.ip13.main

import java.io.File

fun loadAsString(filePath: String): String {
    return File("src/test/resources/$filePath").readText()
}