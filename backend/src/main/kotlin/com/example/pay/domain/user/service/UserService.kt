package com.example.pay.domain.user.service

import com.example.pay.domain.user.dto.UserDtoRequest
import com.example.pay.domain.user.entity.User
import com.example.pay.domain.user.repository.UserRepository
import com.example.pay.global.exception.InvalidInputException
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val messageSource: MessageSource
) {

    /**
     * 회원 가입 API
     *
     * 닉네임 및 이메일 중복 여부 검사 후 save
     *
     * @param userDtoRequest
     * @return message
     */
    fun signUp(userDtoRequest: UserDtoRequest): String {
        // 이메일 중복 여부 검사
        var user: User? = userRepository.findByEmail(userDtoRequest.email)
        if (user != null) {
            throw InvalidInputException(
                "email",
                messageSource.getMessage("user.service.signup.fail.existEmail", null, Locale.getDefault())
            )
        }
        // 닉네임 중복 여부 검사
        user = userRepository.findByNickname(userDtoRequest.nickname)
        if (user != null) {
            throw InvalidInputException(
                "nickname",
                messageSource.getMessage("user.service.signup.fail.existNickname", null, Locale.getDefault())
            )
        }

        user = userDtoRequest.toEntity()
        val savedUser = userRepository.save(user)
        return messageSource.getMessage("user.service.signup.success", null, Locale.getDefault())
    }

    /**
     * 이메일 중복 검사
     *
     * @param email
     * @return boolean
     */
    fun emailCheck(email: String): Boolean {
        // 이메일 중복 여부 검사
        var user: User? = userRepository.findByEmail(email)
        if (user != null) {
            return false
        }
        return true
    }

    /**
     * 닉네임 중복 검사
     *
     * @param nickname
     * @return boolean
     */
    fun nicknameCheck(nickname: String): Boolean {
        // 닉네임 중복 여부 검사
        var user: User? = userRepository.findByNickname(nickname)
        if (user != null) {
            return false
        }
        return true
    }
}
