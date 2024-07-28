package com.almland.vaadinplayground.todo.repostirory

import com.almland.vaadinplayground.todo.domain.Todo
import jakarta.annotation.PostConstruct
import java.time.LocalDateTime
import org.springframework.stereotype.Repository

@Repository
internal class InMemoryRepository {
    companion object {
        private val TODOS: MutableCollection<Todo> = mutableListOf()
    }

    @PostConstruct
    fun setUp() {
        repeat(10) {
            addTodo(
                Todo(
                    "Title $it",
                    "Body $it",
                    "Author $it",
                    LocalDateTime.now().plusDays(it.toLong())
                )
            )
        }
    }

    fun addTodo(todo: Todo) {
        TODOS.add(todo)
    }

    fun removeTodos(todos: Set<Todo>) {
        TODOS.removeAll(todos)
    }

    fun getTodos(): MutableCollection<Todo> = TODOS
}
