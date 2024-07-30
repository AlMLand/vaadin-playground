package com.almland.vaadinplayground.todo.service.export.pdf

import com.almland.vaadinplayground.todo.domain.Todo
import java.io.ByteArrayOutputStream
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine

@Service
internal class PdfGenerator(private val springTemplateEngine: SpringTemplateEngine) {

    fun createPdf(todos: Set<Todo>): ByteArrayOutputStream =
        HtmlToPdfConverter.createPdf(renderHtmlToPdf(todos))

    private fun renderHtmlToPdf(todos: Set<Todo>): String =
        Context()
            .apply { setVariables(mapOf("items" to todos, "title" to "Todos list ${todos.size}")) }
            .let { springTemplateEngine.process("pdf/todos.html", it) }
}