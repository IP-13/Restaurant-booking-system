package com.ip13.main.service

import com.ip13.main.exceptionHandling.exception.UserNotFoundException
import com.ip13.main.model.dto.request.RoleAddRequestDto
import com.ip13.main.model.enums.Role
import com.ip13.main.security.model.entity.User
import com.ip13.main.security.repository.UserRepository
import com.ip13.main.security.service.TokenService
import com.ip13.main.security.service.UserService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull

@ExtendWith(MockKExtension::class)
class UserServiceTest {
    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var tokenService: TokenService

    @InjectMockKs
    private lateinit var userService: UserService

    @Test
    fun loadByUsernameSuccessfullyTest() {
        val username = "username"

        every { userRepository.findByUsername(username) } returns User(username = username)

        val user = userService.loadUserByUsername(username)

        assertAll(
                { Assertions.assertThat(user.username).isEqualTo("username") }
        )

        verify(exactly = 1) { userRepository.findByUsername(any()) }
    }

    @Test
    fun `when findByUserName returns null should throw UserNotFoundException`() {
        val username = "invalid user"

        every { userRepository.findByUsername(username) } returns null

        assertAll(
                {
                    Assertions.assertThatExceptionOfType(UserNotFoundException::class.java)
                            .isThrownBy { userService.loadUserByUsername(username) }
                            .withMessage("No user with name \'invalid user\'")
                }
        )

        verify(exactly = 1) { userRepository.findByUsername(any()) }
    }

    @Test
    fun saveTest() {
        val user = User(username = "ip13")

        every { userRepository.save(user) } returns User(id = 155)

        val savedUser = userRepository.save(user)

        assertAll(
                { Assertions.assertThat(savedUser.id).isEqualTo(155) }
        )

        verify(exactly = 1) { userRepository.save(any()) }
    }

    @Test
    fun findByIdOrNullTest() {
        val id = 13

        every { userRepository.findByIdOrNull(id) } returns User(username = "ip13")

        val foundUser = userRepository.findByIdOrNull(id)

        assertAll(
                { Assertions.assertThat(foundUser!!.username).isEqualTo("ip13") }
        )

        verify(exactly = 1) { userRepository.findById(any()) }
    }

    @Test
    fun findAllTest() {
        val users = listOf(User(username = "first user"), User(username = "second user"))

        every { userRepository.findAll() } returns users

        val allUsers = userService.findAll()

        assertAll(
                { Assertions.assertThat(allUsers).hasSize(2) },
                { Assertions.assertThat(allUsers.component1().username).isEqualTo("first user") },
                { Assertions.assertThat(allUsers.component2().username).isEqualTo("second user") },
        )

        verify(exactly = 1) { userRepository.findAll() }
    }

    @Test
    fun `when add role to user which doesn't has that role should return true`() {
        val id = 10
        val username = "ip"
        val roles = mutableListOf<Role>()

        val user = User(id = id, username = username, roles = roles)
        val roleAddRequestDto = RoleAddRequestDto(userId = id, Role.MANAGER)

        every { userRepository.findByIdOrNull(id) } returns user
        every { userRepository.save(user) } returns User()

        val isAdded = userService.addRole(roleAddRequestDto)

        assertAll(
                { Assertions.assertThat(isAdded).isTrue() }
        )

        verify(exactly = 1) { userRepository.save(any()) }
        verify(exactly = 1) { userRepository.findById(any()) }
    }

    @Test
    fun `when add role to user which already has that role should return false`() {
        val id = 10
        val username = "ip"
        val roles = mutableListOf(Role.MANAGER)

        val user = User(id = id, username = username, roles = roles)
        val roleAddRequestDto = RoleAddRequestDto(userId = id, Role.MANAGER)

        every { userRepository.findByIdOrNull(id) } returns user
        every { userRepository.save(user) } returns User()

        val isAdded = userService.addRole(roleAddRequestDto)

        assertAll(
                { Assertions.assertThat(isAdded).isFalse() }
        )

        verify(exactly = 0) { userRepository.save(any()) }
        verify(exactly = 1) { userRepository.findById(any()) }
    }
}