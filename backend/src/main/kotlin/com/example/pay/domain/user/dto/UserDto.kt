package com.example.pay.domain.user.dto

import com.example.pay.domain.user.entity.User
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.util.ResourceBundle

/**
 * 회원가입을 위한 DTO
 *
 * @property _email 이메일 검증 절차
 * @property _password 영문, 숫자, 특수문자를 포함한 8~20자리 검증
 * @property _phone 01 로 시작하는 11자리 핸드폰 번호 검증
 * @property _name
 * @property _nickname
 */
class UserDtoRequest(

    @field:NotBlank
    @field:Email
    @JsonProperty("email")
    private val _email: String?,

    @field:NotBlank
    @field:Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#\$%^&*]{8,20}\$",
        message = "영문, 숫자, 특수문자를 포함한 8~20자리로 입력해주세요"
    )
    @JsonProperty("password")
    private val _password: String?,

    @field:NotBlank
    @JsonProperty("name")
    private val _name: String?,

    @field:NotBlank
    @JsonProperty("name")
    private val _nickname: String?,

    @field:NotBlank
    @field:Pattern(
        regexp = "^01\\d{9}$",
        message = "핸드폰 번호를 다시 확인해주세요"
    )
    @JsonProperty("phone")
    private val _phone: String?,

    ) {

    val password: String
        get() = _password!!
    val name: String
        get() = _name!!

    val nickname: String
        get() = _nickname!!

    val email: String
        get() = _email!!
    val phone: String
        get() = _phone!!

    fun toEntity(): User = User(email = email, password = password, name = name, nickname = nickname, phone = phone)
}