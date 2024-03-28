package com.example.pay.global.status

enum class UserType(val desc: String) {
    NORMAL("일반"),
    POORLY_FED_CHILD("결식아동"),
    NATIONAL_MERIT("국가유공자")
}

enum class ResultCode(val msg: String) {
    SUCCESS("정상 처리 되었습니다."),
    ERROR("에러가 발생했습니다.")
}

enum class Role {
    MEMBER
}

enum class Status(val desc: String) {
    NORMAL("일반"),
    DELETED("삭제"),
    INACTIVE("비활성화"),
    TEMPORARY_DELETED("임시삭제")
}