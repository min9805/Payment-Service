package com.example.pay.domain.user.service

import com.example.pay.domain.user.dto.UserSignupReqDto
import com.example.pay.domain.user.dto.UserUpdateReqDto
import com.example.pay.domain.user.entity.User
import com.example.pay.domain.user.repository.UserRepository
import com.example.pay.domain.user.repository.UserRoleRepository
import com.example.pay.global.authority.JwtTokenProvider
import com.example.pay.global.dto.CustomUser
import com.example.pay.global.exception.InvalidInputException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.springframework.context.MessageSource
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceTest() : BehaviorSpec() {
    lateinit var userRepository: UserRepository
    lateinit var userRoleRepository: UserRoleRepository
    lateinit var authenticationManagerBuilder: AuthenticationManagerBuilder
    lateinit var jwtTokenProvider: JwtTokenProvider
    lateinit var messageSource: MessageSource
    lateinit var passwordEncoder: PasswordEncoder

    lateinit var target: UserService

    init {
        beforeTest {
            userRepository = mockk()
            userRoleRepository = mockk()
            messageSource = mockk()
            jwtTokenProvider = mockk()
            authenticationManagerBuilder = mockk()
            passwordEncoder = spyk<BCryptPasswordEncoder>()

            target = UserService(
                userRepository,
                userRoleRepository,
                authenticationManagerBuilder,
                jwtTokenProvider,
                messageSource,
                passwordEncoder
            )
        }

        afterContainer {
            clearAllMocks()
        }

        Given("회원 가입을 진행할 때")
        {
            When("이미 등록된 Email 이라면") {
                every { userRepository.findByEmail(any()) } returns mockk<User>()

                val result = target.emailCheck(userSignupReqDto.email)

                Then("false 를 반환한다.") {
                    result shouldBe false
                }
            }

            When("등록되지 않은 Email 이라면") {
                every { userRepository.findByEmail(any()) } returns null

                val result = target.emailCheck(userSignupReqDto.email)

                Then("true 를 반환한다.") {
                    result shouldBe true
                }
            }

            When("이미 등록된 nickname 이라면") {
                every { userRepository.findByNickname(any()) } returns mockk<User>()

                val result = target.nicknameCheck(userSignupReqDto.nickname)

                Then("false 를 반환한다.") {
                    result shouldBe false
                }
            }

            When("등록되지 않은 nickname 이라면") {
                every { userRepository.findByNickname(any()) } returns null

                val result = target.nicknameCheck(userSignupReqDto.nickname)

                Then("true 를 반환한다.") {
                    result shouldBe true
                }
            }


            When("정상적인 유저 정보가 입력되었다면") {
                every { messageSource.getMessage(any(), any(), any()) } returns SignUp_msg_success
                every { userRepository.findByEmail(userSignupReqDto.email) } returns null
                every { userRepository.findByNickname(userSignupReqDto.nickname) } returns null
                every { userRepository.save(any()) } answers { firstArg() }
                every { userRoleRepository.save(any()) } answers { firstArg() }

                val result = target.signUp(userSignupReqDto)

                Then("정상적으로 회원가입이 완료되어야한다") {
                    result shouldBe SignUp_msg_success
                }
            }

            When("저장 시 이미 등록된 Email 유저 정보가 입력되었다면") {
                Then("InvalidInputException 이 발생한다.") {
                    every { messageSource.getMessage(any(), any(), any()) } returns SignUp_msg_fail_existed_email
                    every { userRepository.findByEmail(any()) } returns mockk<User>()

                    shouldThrow<InvalidInputException> {
                        val result = target.signUp(userSignupReqDto)
                        result shouldBe SignUp_msg_fail_existed_email
                    }
                }
            }
        }

        Given("회원 정보 수정 시") {
            When("닉네임 변경 시 중복되었다면") {
                Then("Exception 을 발생한다") {
                    every { userRepository.save(any()) } answers { firstArg() }
                    every { (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId } returns 1
                    
                    val result = target.updateUserInfo(userUpdateReqDto)

                    result shouldBe Update_msg_success

                }
            }

            When("새로운 닉네임으로 변경한다면") {
                Then("정상적으로 닉네임이 변경된다") {

                }
            }

            When("결식 아동 인증이 완료되었다면") {
                Then("해당 유저의 type 을 결식아동으로 변경한다") {

                }
            }

            When("국가 유공자 인증이 완료되었다면") {
                Then("해당 유저의 type 을 국가유공자로 변경한다") {

                }
            }

            When("피기부자라면") {
                Then("자기소개를 작성할 수 있다") {

                }
            }
        }

        Given("회원 탈퇴 시") {
            When("지금 탈퇴한다면") {
                Then("비활성화 상태로 30일 간 보유한다") {

                }
            }

            When("탈퇴한지 30일이 지났다면") {
                Then("삭제 상태로 변경하고 실제 데이터를 삭제한다") {

                }
            }
        }

        Given("회원 정보 조회 시") {
            When("본인의 정보라면") {
                Then("조회할 수 있다.") {

                }
            }

            When("본인의 정보가 아니라면") {
                Then("조회할 수 없다") {

                }
            }

            When("피기부자의 정보라면") {
                Then("제한된 정보를 조회할 수 있다") {

                }
            }

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