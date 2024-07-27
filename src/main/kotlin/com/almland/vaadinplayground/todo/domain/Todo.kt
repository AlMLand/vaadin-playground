package com.almland.vaadinplayground.todo.domain

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import org.springframework.format.annotation.DateTimeFormat

internal data class Todo(
    val title: String,
    val body: String,
    val author: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val closed: Boolean = false
)
