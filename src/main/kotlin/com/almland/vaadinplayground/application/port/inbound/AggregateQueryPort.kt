package com.almland.vaadinplayground.application.port.inbound

import com.almland.vaadinplayground.domain.Todo
import java.io.ByteArrayInputStream

internal interface AggregateQueryPort {
    fun getAll(): Collection<Todo>
    fun getComponentsToShowInPdf(todos: Set<Todo>): Map<String, Any>
    fun createPdfAsStream(htmlTemplate: String): ByteArrayInputStream
    fun createExcelAsStream(todos: Collection<Todo>): ByteArrayInputStream
}
