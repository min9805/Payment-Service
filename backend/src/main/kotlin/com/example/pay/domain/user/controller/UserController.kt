package com.example.pay.domain.user.controller

import com.example.pay.domain.user.dto.LoginReqDto
import com.example.pay.domain.user.dto.UserSignupReqDto
import com.example.pay.domain.user.dto.UserUpdateReqDto
import com.example.pay.domain.user.service.UserService
import com.example.pay.global.authority.TokenInfo
import com.example.pay.global.dto.BaseResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/signup")
    fun signUp(@RequestBody userSignupReqDto: UserSignupReqDto): BaseResponse<Unit> {
        val result = userService.signUp(userSignupReqDto)
        return BaseResponse(message = result)
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    fun login(@RequestBody @Valid loginDto: LoginReqDto): BaseResponse<TokenInfo> {
        val tokenInfo = userService.login(loginDto)
        return BaseResponse(data = tokenInfo)
    }

    @PutMapping("/update")
    fun update(@RequestBody userUpdateReqDto: UserUpdateReqDto): BaseResponse<Unit> {
        val result = userService.updateUserInfo(userUpdateReqDto)
        return BaseResponse(message = result)
    }
}