package com.ip13.main.controller

import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class AdminControllerTest : AbstractTestContainersTest() {
    @Test
    @WithMockUser(authorities = [ADMIN])
    fun `test add role to non-existent user`() {
        val body = loadAsString("json/non_existent_user.json")

        mockMvc.post("/admin/add_role") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            MockMvcResultMatchers.status().`is`(400)
            content {
                jsonPath(
                    "message",
                    CoreMatchers.containsString(
                        "UserNotFoundException: User with id: 10 not found",
                    )
                )
            }
        }
    }
}