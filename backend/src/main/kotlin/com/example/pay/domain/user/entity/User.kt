package com.example.pay.domain.user.entity

import com.example.pay.domain.user.dto.UserUpdateReqDto
import com.example.pay.global.dto.BaseEntity
import com.example.pay.global.status.Status
import com.example.pay.global.status.UserType
import jakarta.persistence.*
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

@Entity
@Table(
    name = "USERS",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_users_email", columnNames = ["email"]),
        UniqueConstraint(name = "uk_users_nickname", columnNames = ["nickname"])
    ],
)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Long? = null,

    @Column(nullable = false, length = 30, updatable = false)
    val email: String = "",

    @Column(nullable = false, length = 100)
    var password: String = "",

    @Column(nullable = false, length = 10)
    val name: String = "",

    @Column(nullable = false, length = 10)
    var nickname: String = "",

    @Column(nullable = false, length = 11)
    var phone: String = "",

    @Column(nullable = false, length = 100)
    var profileImg: String = "",

    @Column(nullable = false, length = 255)
    var description: String = "",

    @Column()
    @Enumerated(EnumType.STRING)
    var userType: UserType? = null,
) : BaseEntity() {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    val userRole: List<UserRole>? = null

    fun encodePassword(passwordEncoder: PasswordEncoder) {
        password = passwordEncoder.encode(password)
    }

    fun update(userUpdateReqDto: UserUpdateReqDto) {
        userUpdateReqDto.password?.let { password = it }
        userUpdateReqDto.nickname?.let { nickname = it }
        userUpdateReqDto.phone?.let { phone = it }
        userUpdateReqDto.profileImg?.let { profileImg = it }
        userUpdateReqDto.description?.let { description = it }
    }
}