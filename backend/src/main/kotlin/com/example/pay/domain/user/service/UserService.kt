package com.example.pay.domain.user.service

import com.example.pay.domain.user.dto.LoginReqDto
import com.example.pay.domain.user.dto.UserSignupReqDto
import com.example.pay.domain.user.dto.UserUpdateReqDto
import com.example.pay.domain.user.entity.User
import com.example.pay.domain.user.entity.UserRole
import com.example.pay.domain.user.repository.UserRepository
import com.example.pay.domain.user.repository.UserRoleRepository
import com.example.pay.global.authority.JwtTokenProvider
import com.example.pay.global.authority.TokenInfo
import com.example.pay.global.dto.CustomUser
import com.example.pay.global.exception.InvalidInputException
import com.example.pay.global.status.Role
import com.example.pay.global.status.UserType
import jakarta.transaction.Transactional
import org.antlr.v4.runtime.Token
import org.springframework.context.MessageSource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userRoleRepository: UserRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val messageSource: MessageSource,
    private val passwordEncoder: PasswordEncoder
) {

    /**
     * 회원 가입 API
     *
     * 닉네임 및 이메일 중복 여부 검사 후 save
     *
     * @param userSignupReqDto
     * @return message
     */
    @Transactional
    fun signUp(userSignupReqDto: UserSignupReqDto): String {
        // 이메일 중복 여부 검사
        val emailDuplicateCheck = emailCheck(userSignupReqDto.email)
        if (!emailDuplicateCheck) {
            throw InvalidInputException(
                "email",
                messageSource.getMessage("user.service.signup.fail.existEmail", null, Locale.getDefault())
            )
        }

        // 닉네임 중복 여부 검사
        val nicknameDuplicateCheck = nicknameCheck(userSignupReqDto.nickname)
        if (!nicknameDuplicateCheck) {
            throw InvalidInputException(
                "nickname",
                messageSource.getMessage("user.service.signup.fail.existNickname", null, Locale.getDefault())
            )
        }

        // 회원 정보 저장
        val user = userSignupReqDto.toEntity()
        user.encodePassword(passwordEncoder)

        val savedUser = userRepository.save(user)

        val memberRole: UserRole = UserRole(null, Role.MEMBER, savedUser)
        userRoleRepository.save(memberRole)

        return messageSource.getMessage("user.service.signup.success", null, Locale.getDefault())
    }

    /**
     * 이메일 중복 검사
     *
     * @param email
     * @return boolean
     */
    @Transactional
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
    @Transactional
    fun nicknameCheck(nickname: String): Boolean {
        // 닉네임 중복 여부 검사
        var user: User? = userRepository.findByNickname(nickname)
        if (user != null) {
            return false
        }
        return true
    }

    /**
     * Email password 를 통해 로그인 시 Authenticate 진행
     *
     * @param loginReqDto
     * @return jwt Token
     */
    @Transactional
    fun login(loginReqDto: LoginReqDto): TokenInfo {
        val authenticationToken = UsernamePasswordAuthenticationToken(loginReqDto.email, loginReqDto.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        return jwtTokenProvider.createToken(authentication)
    }

    /**
     * 유저 정보 업데이트
     *
     * JWT 내 정보로  User 엔티티 획득
     * 해당 정보로
     *
     * @param userUpdateReqDto
     * @return
     */
    @Transactional
    fun updateUserInfo(userUpdateReqDto: UserUpdateReqDto): String {
        // JWT 내 USER
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val user: User = userRepository.findByIdOrNull(userId) ?: throw InvalidInputException(
            "id",
            "회원번호(${userId})가 존재하지 않는 유저입니다."
        )

        user.update(userUpdateReqDto)

        // 닉네임 중복 체크
        val nicknameDuplicateCheck = userUpdateReqDto.nickname?.let { nicknameCheck(it) }
        if (nicknameDuplicateCheck != null && !nicknameDuplicateCheck) {
            throw InvalidInputException(
                "nickname",
                messageSource.getMessage("user.service.signup.fail.existNickname", null, Locale.getDefault())
            )
        }

        userRepository.save(user)

        return messageSource.getMessage("user.service.update.success", null, Locale.getDefault())

    }

}
