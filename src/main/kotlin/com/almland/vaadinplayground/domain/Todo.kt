package com.almland.vaadinplayground.domain

import java.time.LocalDateTime
import java.util.UUID

internal data class Todo(
    val id: UUID,
    val title: String,
    val body: String,
    val author: String,
    val createdAt: LocalDateTime
)
