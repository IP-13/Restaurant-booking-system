package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.CommonException
import com.ip13.main.exceptionHandling.exception.UserNotFoundException
import com.ip13.main.model.dto.request.BlackListRequestDto
import com.ip13.main.model.entity.BlackList
import com.ip13.main.model.toBlackList
import com.ip13.main.repository.BlackListRepository
import com.ip13.main.security.entity.User
import com.ip13.main.security.service.UserService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class BlackListServiceTest {
    @MockK
    private lateinit var userService: UserService

    @MockK
    private lateinit var blackListRepository: BlackListRepository

    @InjectMockKs
    private lateinit var blackListService: BlackListService

    @Test
    fun addCorrectBlackListTest() {
        val defaultUser = User(TEST_USER_ID)
        val dto = BlackListRequestDto(TEST_USER_ID,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "TEST")
        every { userService.findByIdOrThrow(any()) } returns defaultUser
        every { blackListRepository.save(any()) } returns BlackList(TEST_BLACK_LIST_ID)
        assertAll(
                { Assertions.assertEquals(blackListService.processRequest(dto), TEST_BLACK_LIST_ID) }
        )
        assertAll(
                { Assertions.assertEquals(blackListService.save(dto.toBlackList()), TEST_BLACK_LIST_ID) }
        )
    }

    @Test
    fun addNotExistingUserTest() {
        val dto = BlackListRequestDto(TEST_USER_ID,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "TEST")
        every { userService.findByIdOrThrow(any()) } throws UserNotFoundException()
        every { blackListRepository.save(any()) } returns BlackList(TEST_BLACK_LIST_ID)
        assertThrows<UserNotFoundException> { blackListService.processRequest(dto) }
        assertThrows<UserNotFoundException> { blackListService.save(dto.toBlackList()) }
    }

    @Test
    fun addTillYesterdayTest() {
        val defaultUser = User(TEST_USER_ID)
        val dto = BlackListRequestDto(TEST_USER_ID,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                "TEST")
        every { userService.findByIdOrThrow(any()) } returns defaultUser
        every { blackListRepository.save(any()) } returns BlackList(TEST_BLACK_LIST_ID)
        assertThrows<CommonException> { blackListService.processRequest(dto) }
        assertThrows<CommonException> { blackListService.save(dto.toBlackList()) }
    }

    companion object {
        const val TEST_USER_ID = 13
        const val TEST_BLACK_LIST_ID = 17
    }
}