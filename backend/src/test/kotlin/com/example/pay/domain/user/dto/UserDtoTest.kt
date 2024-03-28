package com.example.pay.domain.user.dto

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory


class UserDtoTest : BehaviorSpec({
    lateinit var factory: ValidatorFactory
    lateinit var validator: Validator

    beforeSpec {
        factory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }


    Given("UserDtoRequest 를 사용할 때") {
        When("모든 형식이 올바르다면") {
            val rightDto = UserSignupReqDto("test@test.com", "Password!123", "name", "nickname", "01012341234")
            val result: Set<ConstraintViolation<UserSignupReqDto>> = validator.validate(rightDto)
            result.isEmpty() shouldBe true
        }


        When("이메일 형식이 올바르지 않다면") {
            val emailDto = UserSignupReqDto("test", "Password!123", "name", "nickname", "01012341234")
            Then("예외가 발생한다.") {
                val result: Set<ConstraintViolation<UserSignupReqDto>> = validator.validate(emailDto)
                result.size shouldBe 1
                result.first().message shouldBe "올바른 형식의 이메일 주소여야 합니다"
            }
        }

        When("비밀번호 형식이 올바르지 않다면") {
            val passwordDto = UserSignupReqDto("test@test.com", "Password", "name", "nickname", "01012341234")
            Then("예외가 발생한다.") {
                val result: Set<ConstraintViolation<UserSignupReqDto>> = validator.validate(passwordDto)
                result.size shouldBe 1
                result.first().message shouldBe "영문, 숫자, 특수문자를 포함한 8~20자리로 입력해주세요"
            }
        }

        When("전화 번호가 11자리가 아니라면") {
            val phone_number_size_Dto =
                UserSignupReqDto("test@test.com", "Password!123", "name", "nickname", "0111")
            Then("예외가 발생한다.") {
                val result: Set<ConstraintViolation<UserSignupReqDto>> = validator.validate(phone_number_size_Dto)
                result.size shouldBe 1
                result.first().message shouldBe "핸드폰 번호를 다시 확인해주세요"
            }
        }

        When("전화번호 형식이 올바르지 않다면") {
            val valid_phone_number_Dto =
                UserSignupReqDto("test@test.com", "Password!123", "name", "nickname", "11384728592")
            Then("예외가 발생한다.") {
                val result: Set<ConstraintViolation<UserSignupReqDto>> = validator.validate(valid_phone_number_Dto)
                result.size shouldBe 1
                result.first().message shouldBe "핸드폰 번호를 다시 확인해주세요"
            }
        }
    }

})