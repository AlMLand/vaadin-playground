package com.almland.vaadinplayground.application.port.inbound

import com.almland.vaadinplayground.domain.Todo
import java.io.InputStream
import java.util.UUID

internal interface AggregateCommandPort {
    fun create(todo: Todo): UUID
    fun deleteAll(ids: Collection<UUID>)
    fun createFromStream(inputStream: InputStream): Int
}
