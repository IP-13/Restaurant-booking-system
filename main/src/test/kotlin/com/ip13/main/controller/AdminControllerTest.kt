//package com.ip13.main.controller
//
//import com.ip13.main.security.configuration.JwtRequestFilter
//import com.ip13.main.security.service.TokenService
//import com.ip13.main.security.service.UserService
//import io.mockk.every
//import io.mockk.just
//import io.mockk.mockk
//import io.mockk.runs
//import org.junit.jupiter.api.Test
//import org.junit.runner.RunWith
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
//import org.springframework.boot.test.mock.mockito.MockBean
//import org.springframework.security.test.context.support.WithMockUser
//import org.springframework.test.context.junit4.SpringRunner
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
//
//@RunWith(SpringRunner::class)
//@WebMvcTest(
//    AdminControllerTest::class,
////    properties = ["security.enabled=false"],
//)
//class AdminControllerTest {
//    @Autowired
//    private lateinit var mockMvc: MockMvc
//
//    private val userService: UserService = mockk()
//
//    @MockBean
//    private lateinit var jwtRequestFilter: JwtRequestFilter
//
//    @MockBean
//    private lateinit var tokenService: TokenService
//
//    @Test
//    @WithMockUser(authorities = ["admin", "ADMIN"])
//    fun `test add role with user who already has role`() {
//        every { userService.checkUser(any()) } just runs
//        every { userService.checkRole(any(), any()) } returns true
//
//        val content = "{ \"userId\": 10, \"role\": \"manager\" }"
//        val result = mockMvc.perform(
//            MockMvcRequestBuilders
//                .post("/admin/add_role")
////                .with(jwt())
//                .content(content)
////                .header("Bearer ", "1234")
//                .contentType("application/json;charset=UTF-8")
//        ).andReturn()
//
//        println("_____________________________________________________________________________________________________")
//        println(result.response.errorMessage)
//        println(result.response.status)
//        println(result.response.contentAsString)
//        println("_____________________________________________________________________________________________________")
//    }
//}