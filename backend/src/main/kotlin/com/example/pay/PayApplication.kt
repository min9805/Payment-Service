package com.example.pay

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class PayApplication

fun main(args: Array<String>) {
    runApplication<PayApplication>(*args)
}
