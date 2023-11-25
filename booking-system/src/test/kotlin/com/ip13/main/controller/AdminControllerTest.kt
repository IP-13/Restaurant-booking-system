package com.ip13.main.controller

import com.ip13.main.model.enums.Role
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class AdminControllerTest : AbstractTestContainersTest() {
    @Test
    @WithMockUser(authorities = [ADMIN])
    fun `test add role to non-existent user`() {
        val body = loadAsString("json/add_role_to_non_existent_user.json")

        mockMvc.post("/admin/add-role") {
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

    @Test
    @WithMockUser(authorities = [ADMIN])
    fun addRoleTest() {
        createDefaultUser()

        val body = loadAsString("json/add_role_request.json")

        mockMvc.post("/admin/add-role") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            MockMvcResultMatchers.status().`is`(200)
            content {
                jsonPath(
                    "response",
                    CoreMatchers.containsString(
                        "Role MANAGER successfully added to user ",
                    )
                )
            }
        }

        val users = userRepository.findAll()

        assertAll(
            { assertThat(users).hasSize(1) },
            { assertThat(users.first().roles).hasSize(1) },
            { assertThat(users.first().roles.first()).isEqualTo(Role.MANAGER) },
        )
    }

    @Test
    @WithMockUser(authorities = [ADMIN])
    fun deleteRoleTest() {
        createDefaultUser()

        val addRoleBody = loadAsString("json/add_role_request.json")

        mockMvc.post("/admin/add-role") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = addRoleBody
        }.andExpect {
            MockMvcResultMatchers.status().`is`(200)
            content {
                jsonPath(
                    "response",
                    CoreMatchers.containsString(
                        "Role MANAGER successfully added to user ",
                    )
                )
            }
        }

        val users = userRepository.findAll()

        assertAll(
            { assertThat(users).hasSize(1) },
            { assertThat(users.first().roles).hasSize(1) },
            { assertThat(users.first().roles.first()).isEqualTo(Role.MANAGER) },
        )

        val deleteRoleBody = loadAsString("json/delete_role_request.json")

        mockMvc.post("/admin/delete-role") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = deleteRoleBody
        }.andExpect {
            MockMvcResultMatchers.status().`is`(200)
            content {
                jsonPath(
                    "response",
                    CoreMatchers.containsString(
                        "Role MANAGER successfully deleted from user",
                    )
                )
            }
        }

        val usersAfterDeleteRole = userRepository.findAll()

        assertAll(
            { assertThat(usersAfterDeleteRole).hasSize(1) },
            { assertThat(usersAfterDeleteRole.first().roles).hasSize(0) },
        )
    }

    @Test
    @WithMockUser(authorities = [ADMIN])
    fun addToBlackListTest() {
        createDefaultUser()

        // TODO() what to do with dates? Update test when date expires?
        val body = loadAsString("json/add_to_black_list_request.json")

        mockMvc.post("/admin/add-to-black-list") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            MockMvcResultMatchers.status().`is`(200)
            content {
                jsonPath(
                    "blackListId",
                    CoreMatchers.equalToObject(100)
                )
            }
        }
    }
}