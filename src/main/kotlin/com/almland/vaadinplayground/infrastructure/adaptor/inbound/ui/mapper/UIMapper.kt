package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.mapper

import com.almland.vaadinplayground.domain.Todo
import com.almland.vaadinplayground.domain.TodoBuilder
import org.springframework.stereotype.Service

@Service
internal class UIMapper {
    fun mapToTodo(title: String, body: String, userName: String): Todo =
        TodoBuilder
            .createTodo(title, body, userName)
}