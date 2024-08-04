package com.almland.vaadinplayground.application.port.inbound

import com.almland.vaadinplayground.domain.Todo
import java.io.ByteArrayInputStream

internal interface AggregateQueryPort {
    fun getAll(): Collection<Todo>
    fun getImageAsBase64(imagePath: String): String
    fun getBarCodeAsBase64(text: String): String
    fun getPdfAsStream(htmlTemplate: String): ByteArrayInputStream
    fun getExcelAsStream(todos: Collection<Todo>): ByteArrayInputStream
}
