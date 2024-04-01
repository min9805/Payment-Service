package com.example.pay.domain.user.repository

import com.example.pay.domain.user.entity.User
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.mockk
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest(
    @Autowired private val userRepository: UserRepository
) : BehaviorSpec() {

    init {

        beforeSpec {
            userRepository.save(
                User(
                    email = "user@email.com", password = "password123!",
                    name = "name", nickname = "nickname", phone = "01012345678",
                    profileImg = "profileImg", description = "description", userType = null
                )
            )
        }

        afterSpec {
            userRepository.deleteAllInBatch()
        }

        given("UserRepository") {
            `when`("findByEmail 을 했을 때 이메일 유저가 있다면") {
                then("should return User") {
                    val findByEmail = userRepository.findByEmail("user@email.com")
                    findByEmail?.let { findByEmail.email shouldBe "user@email.com" }
                }
            }
            `when`("findByEmail 을 했을 때 이메일 유저가 없다면") {
                then("should return null") {
                    val findByEmail = userRepository.findByEmail("NotExistsEmail")
                    findByEmail shouldBe null
                }
            }
            `when`("findByNickname 을 했을 때 닉네임 유저가 있다면") {
                then("should return User") {
                    val findByNickname = userRepository.findByNickname("nickname")
                    findByNickname?.let { findByNickname.nickname shouldBe "nickname" }
                }
            }
            `when`("findByNickname 을 했을 때 닉네임 유저가 없다면") {
                then("should return null") {
                    val findByNickname = userRepository.findByNickname("NotExistsEmail")
                    findByNickname shouldBe null
                }
            }
        }
    }

    //https://isaac56.github.io/jpa/2021/05/29/JPA-using-test-db/

}