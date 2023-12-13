package com.ip13.main.controller

import com.ip13.main.webClient.blackListClient.BlackListServiceWebClient
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ExtendWith(MockKExtension::class)
class ReserveControllerTest : AbstractTestContainer() {
    @MockK
    private lateinit var blackListServiceWebClient: BlackListServiceWebClient

    @Test
    @WithMockUser
    fun `reserve table in non-existent restaurant`() {
        every { blackListServiceWebClient.getBlackListByUsername(any(), any()) } returns listOf()

        val body = loadAsString("json/reserve-table.json")

        mockMvc.post("/reservation/reserve-table") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
            header("Authorization", "Bearer 12341234")
        }.andExpect {
            MockMvcResultMatchers.status().`is`(404)
            content {
                jsonPath(
                    "message",
                    CoreMatchers.containsString(
                        "No restaurant with id 100",
                    )
                )
            }
        }
    }
}