package com.example.pay.global.service

import com.example.pay.domain.user.entity.User
import com.example.pay.domain.user.repository.UserRepository
import com.example.pay.global.dto.CustomUser
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(userEmail: String): UserDetails =
        userRepository.findByEmail(userEmail)
            ?.let { createUserDetails(it) } ?: throw UsernameNotFoundException("해당 유저는 없습니다.")


    private fun createUserDetails(user: User): UserDetails =
        CustomUser(
            user.userId!!,
            user.email,
            user.password,
            user.userRole!!.map { SimpleGrantedAuthority("ROLE_${it.role}") }
        )
}