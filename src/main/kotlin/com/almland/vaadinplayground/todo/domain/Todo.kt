package com.almland.vaadinplayground.todo.domain

import java.time.LocalDateTime

internal data class Todo(
    val title: String,
    val body: String,
    val author: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val closed: Boolean = false
)
