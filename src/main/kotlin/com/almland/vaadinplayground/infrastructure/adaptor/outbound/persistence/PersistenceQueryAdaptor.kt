package com.almland.vaadinplayground.infrastructure.adaptor.outbound.persistence

import com.almland.vaadinplayground.application.port.outbound.PersistenceQueryPort
import com.almland.vaadinplayground.domain.Todo
import com.almland.vaadinplayground.infrastructure.adaptor.outbound.persistence.mapper.TodoMapper
import com.almland.vaadinplayground.infrastructure.adaptor.outbound.persistence.repository.TodoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
internal class PersistenceQueryAdaptor(
    private val todoMapper: TodoMapper,
    private val todoRepository: TodoRepository
) : PersistenceQueryPort {

    override fun getAll(): Collection<Todo> =
        todoRepository
            .findAll()
            .let { todoMapper.mapToTodos(it) }
}
