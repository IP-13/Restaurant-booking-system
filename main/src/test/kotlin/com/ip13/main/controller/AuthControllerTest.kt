package com.ip13.main.controller

import com.ip13.main.model.enums.Role
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthControllerTest : AbstractTestContainersTest() {
    @Test
    @WithMockUser(authorities = [ADMIN])
    fun `should add new user to db when register successfully`() {
        val body = loadAsString("json/default_user_register.json")

        mockMvc.post("/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status().`is`(200)
            content {
                // проверка что приходит токен
                jsonPath(
                    "token",
                    containsString(""),
                )
            }
        }

        val user = userRepository.findByUsername("ip13")!!

        assertAll(
            { assertThat(passwordEncoder.matches("Ip13!", user.password)).isTrue() },
            { assertThat(user.username).isEqualTo("ip13") },
            { assertThat(user.numOfGrades).isEqualTo(0) },
            { assertThat(user.sumOfGrades).isEqualTo(0) },
            { assertThat(user.roles).isEqualTo(listOf<Role>()) },
        )
    }

    @Test
    @WithMockUser(authorities = [ADMIN])
    fun `should return 400 status code when register with username that already exists`() {
        createDefaultUser()

        val body = loadAsString("json/default_user_register.json")

        mockMvc.post("/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status().`is`(400)
            content {
                jsonPath(
                    "message",
                    containsString("User with username ip13 already exists"),
                )
            }
        }

        val numOfUsersInDb = jdbc.queryForObject("select count(*) from user_t", Int::class.java)

        assertThat(numOfUsersInDb!!).isEqualTo(1)
    }

    @Test
    fun `should return token when login with valid name and password`() {
        val user = createDefaultUser()

        val body = loadAsString("json/default_user_register.json")

        mockMvc.post("/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status().`is`(200)
            // assert that response contains token
            content {
                jsonPath(
                    "token",
                    containsString(""),
                )
            }
        }
    }

    @Test
    fun `should return 400 status code when wrong password`() {
        createDefaultUser()

        val body = loadAsString("json/default_user_with_wrong_password.json")

        mockMvc.post("/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status().`is`(400)
            // assert that response contains token
            content {
                jsonPath(
                    "message",
                    containsString("passwords don't match"),
                )
            }
        }
    }
}