package com.almland.vaadinplayground.application.port.outbound

import com.almland.vaadinplayground.domain.Todo
import java.util.UUID

internal interface PersistenceCommandPort {
    fun create(todo: Todo): UUID
    fun deleteAll(ids: Collection<UUID>)
}
