package com.almland.vaadinplayground.infrastructure.adaptor.outbound.persistence

import com.almland.vaadinplayground.application.port.outbound.PersistenceCommandPort
import com.almland.vaadinplayground.domain.Todo
import com.almland.vaadinplayground.infrastructure.adaptor.outbound.persistence.mapper.TodoMapper
import com.almland.vaadinplayground.infrastructure.adaptor.outbound.persistence.repository.TodoRepository
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
internal class PersistenceCommandAdaptor(
    private val todoMapper: TodoMapper,
    private val todoRepository: TodoRepository
) : PersistenceCommandPort {

    override fun create(todo: Todo): UUID =
        todoMapper
            .mapToTodoEntity(todo)
            .let { todoRepository.save(it).id }

    override fun createAll(todos: Collection<Todo>): Int =
        todoMapper
            .mapToTodoEntities(todos)
            .let { todoRepository.saveAll(it).size }

    override fun deleteAll(ids: Collection<UUID>) =
        todoRepository.deleteAllById(ids)
}
