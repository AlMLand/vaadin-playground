package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button.download

import com.almland.vaadinplayground.application.port.inbound.AggregateQueryPort
import com.almland.vaadinplayground.domain.Todo
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button.download.DownloadPdfButtonBuilder.PdfComponent.BARCODE
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button.download.DownloadPdfButtonBuilder.PdfComponent.ITEMS
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button.download.DownloadPdfButtonBuilder.PdfComponent.KENNY
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button.download.DownloadPdfButtonBuilder.PdfComponent.TITLE
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.server.StreamResource
import java.io.ByteArrayInputStream
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine

internal object DownloadPdfButtonBuilder {

    private const val FILE_PDF_NAME = "todos.pdf"
    private const val OPEN_PDF_IN_NEW_TAB = "_blank"
    private const val TEMPLATE_TARGET_PATH = "pdf/todos.html"
    private const val DOWNLOAD_BUTTON_TEXT = "Export selected to PDF"

    private enum class PdfComponent(val component: String) {
        ITEMS("items"), TITLE("title"), KENNY("kenny"), BARCODE("barcode")
    }

    fun create(
        grid: Grid<Todo>,
        aggregateQueryPort: AggregateQueryPort,
        springTemplateEngine: SpringTemplateEngine
    ): Anchor =
        Anchor(StreamResource(FILE_PDF_NAME, getPdfAsStream(grid, aggregateQueryPort, springTemplateEngine)), null)
            .apply { setTarget(OPEN_PDF_IN_NEW_TAB) }
            .also { downloadPdf ->
                Button(DOWNLOAD_BUTTON_TEXT)
                    .apply { addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL) }
                    .also { downloadPdf.add(it) }
            }

    private fun getPdfAsStream(
        grid: Grid<Todo>,
        aggregateQueryPort: AggregateQueryPort,
        springTemplateEngine: SpringTemplateEngine
    ): () -> ByteArrayInputStream = {
        Context()
            .apply { setVariables(getComponentsToShowInPdf(grid.selectedItems, aggregateQueryPort)) }
            .let { springTemplateEngine.process(TEMPLATE_TARGET_PATH, it) }
            .let { aggregateQueryPort.getPdfAsStream(it) }
    }

    private fun getComponentsToShowInPdf(
        todos: Set<Todo>,
        aggregateQueryPort: AggregateQueryPort
    ): Map<String, Any> =
        mapOf(
            ITEMS.component to todos,
            TITLE.component to "Todos list ${todos.size}",
            KENNY.component to aggregateQueryPort.getImageAsBase64("pdf/images/kenny.png"),
            BARCODE.component to aggregateQueryPort.getBarCodeAsBase64("Todo-list ${todos.size}")
        )
}
