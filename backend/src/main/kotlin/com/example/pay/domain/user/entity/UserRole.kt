package com.example.pay.domain.user.entity

import com.example.pay.global.dto.BaseEntity
import com.example.pay.global.status.Role
import com.example.pay.global.status.Status
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "USER_ROLES")
class UserRole(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userRoleId: Long? = null,

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    val role: Role,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = ForeignKey(name = "fk_user_roles_id"))
    val user: User,
) : BaseEntity() {}