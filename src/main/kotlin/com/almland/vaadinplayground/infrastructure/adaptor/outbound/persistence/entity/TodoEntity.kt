package com.almland.vaadinplayground.infrastructure.adaptor.outbound.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "todo")
internal class TodoEntity(
    @Id
    @Column(updatable = false, nullable = false)
    val id: UUID,
    @Column(updatable = false, nullable = false)
    val title: String,
    @Column(updatable = false, nullable = false)
    val body: String,
    @Column(updatable = false, nullable = false)
    val author: String,
    @Column(updatable = false, nullable = false)
    val createdAt: LocalDateTime
)
