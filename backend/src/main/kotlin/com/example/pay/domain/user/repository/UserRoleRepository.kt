package com.example.pay.domain.user.repository

import com.example.pay.domain.user.entity.UserRole
import org.springframework.data.jpa.repository.JpaRepository

interface UserRoleRepository : JpaRepository<UserRole, Long> {
}