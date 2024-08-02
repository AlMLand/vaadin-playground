package com.almland.vaadinplayground.application.aggregate.export.pdf

import com.almland.vaadinplayground.application.aggregate.export.pdf.PdfGenerator.PdfComponent.BARCODE
import com.almland.vaadinplayground.application.aggregate.export.pdf.PdfGenerator.PdfComponent.ITEMS
import com.almland.vaadinplayground.application.aggregate.export.pdf.PdfGenerator.PdfComponent.KENNY
import com.almland.vaadinplayground.application.aggregate.export.pdf.PdfGenerator.PdfComponent.TITLE
import com.almland.vaadinplayground.application.aggregate.export.pdf.barcode.BarcodeUtils
import com.almland.vaadinplayground.application.aggregate.export.pdf.image.ImageUtils
import com.almland.vaadinplayground.application.port.filegenerator.FileGenerator
import com.almland.vaadinplayground.domain.Todo
import org.xhtmlrenderer.pdf.ITextRenderer

internal class PdfGenerator : FileGenerator<String, ITextRenderer> {

    private enum class PdfComponent(val component: String) {
        ITEMS("items"), TITLE("title"), KENNY("kenny"), BARCODE("barcode")
    }

    override fun createFile(content: String): ITextRenderer =
        ITextRenderer().apply {
            setDocumentFromString(content)
            layout()
        }

    fun getComponentsToShowInPdf(todos: Set<Todo>): Map<String, Any> =
        mapOf(
            ITEMS.component to todos,
            TITLE.component to "Todos list ${todos.size}",
            KENNY.component to ImageUtils.convertImageToBase64("pdf/images/kenny.png"),
            BARCODE.component to BarcodeUtils.getBarCodeAsBase64("Todo-list ${todos.size}")
        )
}
