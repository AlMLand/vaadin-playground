package com.almland.vaadinplayground.domain

import java.time.LocalDateTime
import java.util.UUID

internal object TodoBuilder {
    fun createTodo(
        title: String,
        body: String,
        userName: String,
        id: UUID = UUID.randomUUID(),
        createdAt: LocalDateTime = LocalDateTime.now()
    ): Todo =
        Todo(id, title, body, userName, createdAt)
}
