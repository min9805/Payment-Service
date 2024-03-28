package com.example.pay.global.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUser(
    val userId: Long,
    userEmail: String,
    password: String,
    authorities: Collection<GrantedAuthority>
) : User(userEmail, password, authorities)