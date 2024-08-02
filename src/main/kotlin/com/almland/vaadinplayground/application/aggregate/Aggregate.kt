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

    override fun create(todo: Todo): UUID = persistenceCommandPort.create(todo)

    override fun deleteAll(ids: Collection<UUID>) = persistenceCommandPort.deleteAll(ids)

    override fun getComponentsToShowInPdf(todos: Set<Todo>): Map<String, Any> =
        pdfGenerator.getComponentsToShowInPdf(todos.toSet())

    override fun createPdfAsStream(htmlTemplate: String): ByteArrayInputStream =
        ByteArrayOutputStream()
            .apply { pdfGenerator.createFile(htmlTemplate).createPDF(this) }
            .let { ByteArrayInputStream(it.use { stream -> stream.toByteArray() }) }

    override fun createExcelAsStream(todos: Collection<Todo>): ByteArrayInputStream =
        ByteArrayOutputStream()
            .apply { excelGenerator.createFile(todos).write(this) }
            .let { ByteArrayInputStream(it.use { stream -> stream.toByteArray() }) }
}
