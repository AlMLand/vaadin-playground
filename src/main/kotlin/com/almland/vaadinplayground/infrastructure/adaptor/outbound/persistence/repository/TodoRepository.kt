package com.almland.vaadinplayground.infrastructure.adaptor.outbound.persistence.repository

import com.almland.vaadinplayground.infrastructure.adaptor.outbound.persistence.entity.TodoEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

internal interface TodoRepository : JpaRepository<TodoEntity, UUID>
