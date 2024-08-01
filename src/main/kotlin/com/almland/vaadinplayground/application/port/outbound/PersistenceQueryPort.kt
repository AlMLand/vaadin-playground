package com.almland.vaadinplayground.application.port.outbound

import com.almland.vaadinplayground.domain.Todo

internal interface PersistenceQueryPort {
    fun getAll(): Collection<Todo>
}
