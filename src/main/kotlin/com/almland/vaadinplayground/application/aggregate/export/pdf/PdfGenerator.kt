package com.almland.vaadinplayground.application.aggregate.export.pdf

import com.almland.vaadinplayground.application.port.filegenerator.FileGenerator
import org.xhtmlrenderer.pdf.ITextRenderer

internal class PdfGenerator : FileGenerator<String, ITextRenderer> {

    override fun createFile(content: String?): ITextRenderer =
        ITextRenderer().apply {
            setDocumentFromString(content)
            layout()
        }
}
