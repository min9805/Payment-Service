package com.example.pay.domain.user.entity

import com.example.pay.global.status.Role
import com.example.pay.global.status.Status
import com.example.pay.global.status.UserType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(name = "uk_user_email", columnNames = ["email"])]
)
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 30)
    val email: String,

    @Column(nullable = false, length = 100)
    val password: String,

    @Column(nullable = false, length = 10)
    val name: String,

    @Column(nullable = false, length = 11)
    val phone: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val type: UserType,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val role: Role,

    @Column(nullable = false)
    val createdDate: LocalDateTime,


    @Column(nullable = false)
    val modifiedDate: LocalDateTime,


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val status: Status,
)