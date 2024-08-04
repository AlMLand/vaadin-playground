package com.almland.vaadinplayground.infrastructure.adaptor.outbound.persistence.mapper

import com.almland.vaadinplayground.domain.Todo
import com.almland.vaadinplayground.domain.TodoBuilder
import com.almland.vaadinplayground.infrastructure.adaptor.outbound.persistence.entity.TodoEntity
import org.springframework.stereotype.Service

@Service
internal class TodoMapper {

    fun mapToTodos(todoEntities: Collection<TodoEntity>): Collection<Todo> =
        todoEntities
            .map { TodoBuilder.createTodo(it.title, it.body, it.author, it.id, it.createdAt) }

    fun mapToTodoEntity(todo: Todo): TodoEntity =
        with(todo) { TodoEntity(id, title, body, author, createdAt) }

    fun mapToTodoEntities(todos: Collection<Todo>) =
        todos.map { mapToTodoEntity(it) }
}
