package com.almland.vaadinplayground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VaadinPlaygroundApplication

fun main(args: Array<String>) {
	runApplication<VaadinPlaygroundApplication>(*args)
}
