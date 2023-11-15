package com.ip13.main.utils

import org.hamcrest.CoreMatchers
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
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

fun registerDefaultUser(mockMvc: MockMvc) {
    val adminRole = "ADMIN"

    val body = loadAsString("json/default_user_register_dto.json")

    mockMvc.post("/auth/register") {
        contentType = MediaType.APPLICATION_JSON
        accept = MediaType.APPLICATION_JSON
        content = body
        with(
            SecurityMockMvcRequestPostProcessors.user("ip13")
                .password("13579").authorities(SimpleGrantedAuthority(adminRole))
        )
    }.andExpect {
        MockMvcResultMatchers.status().`is`(200)
        content {
            // проверка что приходит токен
            jsonPath(
                "token",
                CoreMatchers.containsString(""),
            )
        }
    }
}
