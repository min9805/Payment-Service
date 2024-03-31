package com.example.pay.domain.user.entity

import com.example.pay.domain.user.dto.UserUpdateReqDto
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.shouldNotBe
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

class UserTest() : BehaviorSpec() {

    init {
        beforeTest {
            testUser = User(
                email = "user@email.com", password = "password123!",
                name = "name", nickname = "nickname", phone = "01012345678",
                profileImg = "profileImg", description = "description", userType = null
            )
        }

        given("정상적인 유저가 존재할 때") {
            `when`("유저의 비밀번호를 암호화하면") {
                then("비밀번호가 암호화되어야 한다") {
                    testUser.encodePassword(passwordEncoder)

                    testUser.password shouldNotBe "password123!"
                    passwordEncoder.matches("password123!", testUser.password) shouldBe true
                }
            }
            `when`("유저 정보 password 만 업데이트 하면") {
                val userUpdateReqDto = UserUpdateReqDto(
                    _password = "password1234!"
                )

                then("비밀번호가 업데이트 되어야 한다") {
                    testUser.update(userUpdateReqDto)

                    testUser.password shouldBe userUpdateReqDto.password
                }
            }
            `when`("유저 정보 nickname 만 업데이트 하면") {
                val userUpdateReqDto = UserUpdateReqDto(
                    _nickname = "newNickname"
                )

                then("nickname 이 업데이트 되어야 한다") {
                    testUser.update(userUpdateReqDto)

                    testUser.nickname shouldBe userUpdateReqDto.nickname
                }
            }
            `when`("유저 정보 phone 만 업데이트 하면") {
                val userUpdateReqDto = UserUpdateReqDto(
                    _phone = "01012345679"
                )

                then("phone 이 업데이트 되어야 한다") {
                    testUser.update(userUpdateReqDto)

                    testUser.phone shouldBe userUpdateReqDto.phone
                }
            }
            `when`("nickname 과 password 만 업데이트하면") {
                val userUpdateReqDto = UserUpdateReqDto(
                    _nickname = "newNickname",
                    _password = "password1234!"
                )

                then("nickname 과 password 가 업데이트 되어야 한다") {
                    testUser.update(userUpdateReqDto)

                    testUser.nickname shouldBe userUpdateReqDto.nickname
                    testUser.password shouldBe userUpdateReqDto.password
                }
            }
        }
    }

    companion object {
        private val passwordEncoder: PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        var testUser = User(
            email = "user@email.com", password = "password123!",
            name = "name", nickname = "nickname", phone = "01012345678",
            profileImg = "profileImg", description = "description", userType = null
        )
    }
}