package com.almland.vaadinplayground.application.aggregate

import com.almland.vaadinplayground.application.aggregate.export.excel.generator.ExcelGenerator
import com.almland.vaadinplayground.application.aggregate.export.excel.reader.ExcelReader
import com.almland.vaadinplayground.application.aggregate.export.pdf.PdfGenerator
import com.almland.vaadinplayground.application.aggregate.export.pdf.barcode.BarcodeUtils
import com.almland.vaadinplayground.application.aggregate.export.pdf.image.ImageUtils
import com.almland.vaadinplayground.application.port.inbound.AggregateCommandPort
import com.almland.vaadinplayground.application.port.inbound.AggregateQueryPort
import com.almland.vaadinplayground.application.port.outbound.PersistenceCommandPort
import com.almland.vaadinplayground.application.port.outbound.PersistenceQueryPort
import com.almland.vaadinplayground.domain.Todo
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.UUID

internal class TodoAggregate(
    private val persistenceQueryPort: PersistenceQueryPort,
    private val persistenceCommandPort: PersistenceCommandPort
) : AggregateQueryPort, AggregateCommandPort {

    private val pdfGenerator = PdfGenerator()
    private val excelGenerator = ExcelGenerator()

    override fun create(todo: Todo): UUID = persistenceCommandPort.create(todo)

    override fun createFromStream(inputStream: InputStream): Int =
        ExcelReader.readFromFile(inputStream)
            .let { persistenceCommandPort.createAll(it) }

    override fun deleteAll(ids: Collection<UUID>) = persistenceCommandPort.deleteAll(ids)

    override fun getAll(): Collection<Todo> = persistenceQueryPort.getAll()

    override fun getImageAsBase64(imagePath: String): String = ImageUtils.convertImageToBase64(imagePath)

    override fun getBarCodeAsBase64(text: String): String = BarcodeUtils.getBarCodeAsBase64(text)

    override fun getPdfAsStream(htmlTemplate: String): ByteArrayInputStream =
        ByteArrayOutputStream()
            .apply { pdfGenerator.createFile(htmlTemplate).createPDF(this) }
            .let { ByteArrayInputStream(it.use { stream -> stream.toByteArray() }) }

    override fun getExcelAsStream(todos: Collection<Todo>): ByteArrayInputStream =
        ByteArrayOutputStream()
            .apply { excelGenerator.createFile(todos).write(this) }
            .let { ByteArrayInputStream(it.use { stream -> stream.toByteArray() }) }
}
