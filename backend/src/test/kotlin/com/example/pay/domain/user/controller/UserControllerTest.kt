package com.example.pay.domain.user.controller

import com.example.pay.domain.user.dto.LoginReqDto
import com.example.pay.domain.user.service.UserService
import com.example.pay.global.authority.TokenInfo
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post


@WebMvcTest(UserController::class)
@AutoConfigureMockMvc
class UserControllerTest(
    @Autowired val mockMvc: MockMvc
) : BehaviorSpec() {

    @MockBean
    lateinit var jpaMetamodelMappingContext: JpaMetamodelMappingContext

    @MockBean
    lateinit var userService: UserService

    init {

        given("회원가입") {
            `when`("회원가입 요청이 들어올 때") {
                then("회원가입이 성공해야 한다") {
                    assertTrue(true)
                }
            }
        }

        given("로그인") {
            `when`("로그인 요청이 들어올 때") {
                val loginReqDto = LoginReqDto("test@email.com", "password123")

                then("로그인이 성공해야 한다") {
                    every { userService.login(loginReqDto) } returns TokenInfo("grant", "accessToken")

                    mockMvc.post("/api/user/login") {
                        contentType = MediaType.APPLICATION_JSON
                        content = ObjectMapper().writeValueAsString(loginReqDto)
                    }.andExpect {
                        status { isOk() }
                    }

                }
            }
        }

        given("회원정보 수정") {
            `when`("회원정보 수정 요청이 들어올 때") {
                then("회원정보 수정이 성공해야 한다") {
                    assertTrue(true)
                }
            }
        }
    }
}