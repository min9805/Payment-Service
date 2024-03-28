package com.example.pay.domain.user.service

import com.example.pay.domain.user.dto.UserDtoRequest
import com.example.pay.domain.user.entity.User
import com.example.pay.domain.user.repository.UserRepository
import com.example.pay.global.exception.InvalidInputException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.MockK
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.context.MessageSource

class UserServiceTest : BehaviorSpec() {
    lateinit var repository: UserRepository
    lateinit var message: MessageSource
    lateinit var target: UserService

    init {
        beforeTest {
            repository = mockk()
            message = mockk()
            target = UserService(repository, message)
        }

        afterContainer {
            clearAllMocks()
        }

        Given("회원 가입을 진행할 때")
        {
            When("이미 등록된 Email 이라면") {
                every { repository.findByEmail(any()) } returns mockk<User>()

                val result = target.emailCheck(userDtoRequest.email)

                Then("false 를 반환한다.") {
                    result shouldBe false
                }
            }

            When("등록되지 않은 Email 이라면") {
                every { repository.findByEmail(any()) } returns null

                val result = target.emailCheck(userDtoRequest.email)

                Then("true 를 반환한다.") {
                    result shouldBe true
                }
            }

            When("이미 등록된 nickname 이라면") {
                every { repository.findByNickname(any()) } returns mockk<User>()

                val result = target.nicknameCheck(userDtoRequest.nickname)

                Then("false 를 반환한다.") {
                    result shouldBe false
                }
            }

            When("등록되지 않은 Email 이라면") {
                every { repository.findByNickname(any()) } returns null

                val result = target.nicknameCheck(userDtoRequest.nickname)

                Then("true 를 반환한다.") {
                    result shouldBe true
                }
            }


            When("정상적인 유저 정보가 입력되었다면") {
                every { message.getMessage(any(), any(), any()) } returns SignUp_msg_success
                every { repository.findByEmail(userDtoRequest.email) } returns null
                every { repository.findByNickname(userDtoRequest.nickname) } returns null
                every { repository.save(any()) } answers { firstArg() }

                val result = target.signUp(userDtoRequest)

                Then("정상적으로 회원가입이 완료되어야한다") {
                    result shouldBe SignUp_msg_success
                }
            }

            When("저장 시 이미 등록된 Email 유저 정보가 입력되었다면") {
                Then("InvalidInputException 이 발생한다.") {
                    every { message.getMessage(any(), any(), any()) } returns SignUp_msg_fail_existed_email
                    every { repository.findByEmail(any()) } returns mockk<User>()

                    shouldThrow<InvalidInputException> {
                        val result = target.signUp(userDtoRequest)
                        result shouldBe SignUp_msg_fail_existed_email
                    }
                }
            }
        }

    }

    companion object {
        private val userDtoRequest = UserDtoRequest(
            _email = "test@test.com",
            _password = "Password!123",
            _name = "John Doe",
            _nickname = "Johnnn",
            _phone = "01012345678"
        )

        private val SignUp_msg_success = "이미 등록된 Email 입니다"
        private val SignUp_msg_fail_existed_email = "이미 등록된 Email 입니다"

    }
}