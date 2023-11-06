package com.ip13.main.controller

import com.ip13.main.model.entity.enums.Role
import com.ip13.main.security.entity.User
import com.ip13.main.security.service.TokenService
import com.ip13.main.security.service.UserService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@ExtendWith(SpringExtension::class)
@RunWith(SpringRunner::class)
@WebMvcTest(
    AdminControllerTest::class,
    properties = ["security.enabled=true"],
)
class AdminControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var tokenService: TokenService

    @Test
    @WithMockUser(authorities = ["ADMIN"])
    fun `test add role with user who already has role`() {
        val jwtToken = "token 123"

        val user = User(
            username = "ip13",
            password = "hello123",
            numOfGrades = 0,
            sumOfGrades = 0,
            roles = listOf(Role.ADMIN)
        )

        Mockito.`when`(tokenService.getTokenFromHeader(anyString())).thenReturn(jwtToken)
        Mockito.`when`(tokenService.getUsername(jwtToken)).thenReturn(user.username)
        Mockito.`when`(userService.loadUserByUsername(user.username)).thenReturn(user)
        Mockito.`when`(tokenService.isTokenValid(jwtToken, user)).thenReturn(true)

        val body = "{ \"userId\": 10, \"role\": \"manager\" }"
        val result = mockMvc.post("/admin/add_role") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = body
        }.andReturn()

        println("_____________________________________________________________________________________________________")
        println(result.response.errorMessage)
        println(result.response.status)
        println(result.response.contentAsString)
        println("_____________________________________________________________________________________________________")
    }
}