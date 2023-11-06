package com.ip13.main

import org.springframework.test.web.servlet.MvcResult
import java.io.File

fun loadAsString(filePath: String): String {
    return File("src/test/resources/$filePath").readText()
}

fun getMvcResultInfo(result: MvcResult) {
    println("__________________________________________________________________________")
    println(result.response.contentAsString)
    println(result.response.errorMessage)
    println(result.response.status)
    println("__________________________________________________________________________")

}