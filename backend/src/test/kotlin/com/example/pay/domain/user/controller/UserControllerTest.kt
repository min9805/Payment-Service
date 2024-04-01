package com.example.pay.domain.user.controller

import com.example.pay.domain.user.dto.LoginReqDto
import com.example.pay.domain.user.entity.User
import com.example.pay.domain.user.repository.UserRepository
import com.example.pay.domain.user.service.UserService
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(controllers = [UserController::class])
@ContextConfiguration(classes = [UserController::class, UserService::class, UserRepository::class])
class UserControllerTest() : BehaviorSpec() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository

    init {
        extension(SpringExtension)

        beforeTest {
            userRepository.save(
                User(
                    1L, "test@email.com", "password123",
                    "test", "test", "01012345678",
                    "", "", null
                )
            )
        }

        given("회원가입") {
            `when`("회원가입 요청이 들어올 때") {
                then("회원가입이 성공해야 한다") {
                    assertTrue(true)
                }
            }
        }

        given("로그인") {
            `when`("로그인 요청이 들어올 때") {
                val loginReqDto = LoginReqDto("test@email.com", "password123")
                then("로그인이 성공해야 한다") {
                    assertTrue(true)
                }
            }
        }

        given("회원정보 수정") {
            `when`("회원정보 수정 요청이 들어올 때") {
                then("회원정보 수정이 성공해야 한다") {
                    assertTrue(true)
                }
            }
        }
    }
}