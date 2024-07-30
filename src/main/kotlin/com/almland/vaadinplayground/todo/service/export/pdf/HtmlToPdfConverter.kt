package com.almland.vaadinplayground.todo.service.export.pdf

import java.io.ByteArrayOutputStream
import org.xhtmlrenderer.pdf.ITextRenderer

internal object HtmlToPdfConverter {

    fun createPdf(html: String): ByteArrayOutputStream =
        ITextRenderer().run {
            setDocumentFromString(html)
            layout()
            ByteArrayOutputStream().also { this.createPDF(it) }
        }
}
