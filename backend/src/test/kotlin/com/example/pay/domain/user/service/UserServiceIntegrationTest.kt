package com.example.pay.domain.user.service

import com.example.pay.domain.user.dto.LoginReqDto
import com.example.pay.domain.user.dto.UserSignupReqDto
import com.example.pay.domain.user.dto.UserUpdateReqDto
import com.example.pay.domain.user.entity.User
import com.example.pay.domain.user.entity.UserRole
import com.example.pay.domain.user.repository.UserRepository
import com.example.pay.domain.user.repository.UserRoleRepository
import com.example.pay.global.authority.JwtTokenProvider
import com.example.pay.global.status.Role
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldNotBe
import io.mockk.mockk
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.MessageSource
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootTest
class UserServiceIntegrationTest : FunSpec() {

    @MockBean
    private lateinit var userRepository: UserRepository

    @MockBean
    private lateinit var userRoleRepository: UserRoleRepository

    @Autowired
    lateinit var authenticationManagerBuilder: AuthenticationManagerBuilder

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var jwtTokenProvider: JwtTokenProvider

    @Autowired
    lateinit var messageSource: MessageSource

    @Autowired
    private lateinit var userService: UserService

    init {
        extension(SpringExtension)

        test("로그인 성공") {
            val userId = 1L
            val expectedUser = User(userId, "test@test.com", passwordEncoder.encode("password123"))
            expectedUser.userRole = listOf(UserRole(1L, Role.MEMBER, expectedUser))

            given(userRepository.findByEmail("test@test.com")).willReturn(expectedUser)
            val loginReqDto = LoginReqDto("test@test.com", "password123")

            val result = userService.login(loginReqDto)
            result.accessToken shouldNotBe null
        }
    }

    companion object {
        private val userSignupReqDto = UserSignupReqDto(
            _email = "test@test.com",
            _password = "Password!123",
            _name = "John Doe",
            _nickname = "Johnnn",
            _phone = "01012345678"
        )
        private val userUpdateReqDto = mockk<UserUpdateReqDto>()


        private val SignUp_msg_success = "회원가입 성공"
        private val SignUp_msg_fail_existed_email = "이미 등록된 Email"
        private val Update_msg_success = "업데이트 성공"
    }
}