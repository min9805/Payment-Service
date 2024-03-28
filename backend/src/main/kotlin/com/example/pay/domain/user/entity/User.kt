package com.example.pay.domain.user.entity

import com.example.pay.global.status.Role
import com.example.pay.global.status.Status
import com.example.pay.global.status.UserType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "USERS",
    uniqueConstraints = [UniqueConstraint(name = "uk_users_email", columnNames = ["email"])]
)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Long? = null,

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
    val userType: UserType? = null,

    @Column(nullable = false)
    val createdDate: LocalDateTime? = null,

    @Column(nullable = false)
    val modifiedDate: LocalDateTime? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val status: Status? = null,
) {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    val userRole: List<UserRole>? = null
}