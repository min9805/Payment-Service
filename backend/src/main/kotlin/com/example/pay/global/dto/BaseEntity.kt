package com.example.pay.global.dto

import com.example.pay.global.status.Status
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected var createdDate: LocalDateTime = LocalDateTime.MIN

    @LastModifiedDate
    @Column(nullable = false)
    protected var modifiedDate: LocalDateTime = LocalDateTime.MIN

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val status: Status = Status.NORMAL
}