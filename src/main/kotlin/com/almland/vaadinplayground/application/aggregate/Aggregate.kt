package com.almland.vaadinplayground.application.aggregate

import com.almland.vaadinplayground.application.aggregate.export.excel.ExcelGenerator
import com.almland.vaadinplayground.application.aggregate.export.pdf.PdfGenerator
import com.almland.vaadinplayground.application.port.inbound.AggregateCommandPort
import com.almland.vaadinplayground.application.port.inbound.AggregateQueryPort
import com.almland.vaadinplayground.application.port.outbound.PersistenceCommandPort
import com.almland.vaadinplayground.application.port.outbound.PersistenceQueryPort
import com.almland.vaadinplayground.domain.Todo
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.UUID

internal class Aggregate(
    private val persistenceQueryPort: PersistenceQueryPort,
    private val persistenceCommandPort: PersistenceCommandPort
) : AggregateQueryPort, AggregateCommandPort {

    private val pdfGenerator = PdfGenerator()
    private val excelGenerator = ExcelGenerator()

    override fun getAll(): Collection<Todo> = persistenceQueryPort.getAll()

    override fun save(todo: Todo): UUID = persistenceCommandPort.save(todo)

    override fun deleteAll(ids: Collection<UUID>) = persistenceCommandPort.deleteAll(ids)

    override fun getComponentsToShowInPdf(todos: Set<Todo>): Map<String, Any> =
        pdfGenerator.getComponentsToShowInPdf(todos.toSet())

    override fun createPdf(htmlTemplate: String): () -> ByteArrayInputStream = {
        ByteArrayInputStream(
            pdfGenerator
                .createFile(htmlTemplate)
                .use { it.toByteArray() }
        )
    }

    override fun getInputStreamExcel(todos: Collection<Todo>): () -> ByteArrayInputStream = {
        ByteArrayOutputStream()
            .apply { excelGenerator.createFile(todos).write(this) }
            .let { ByteArrayInputStream(it.use { stream -> stream.toByteArray() }) }
    }
}
