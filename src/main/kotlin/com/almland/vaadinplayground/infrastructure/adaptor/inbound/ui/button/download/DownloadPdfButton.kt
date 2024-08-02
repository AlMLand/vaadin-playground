package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.button.download

import com.almland.vaadinplayground.application.port.inbound.AggregateQueryPort
import com.almland.vaadinplayground.domain.Todo
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.server.StreamResource
import java.io.ByteArrayInputStream
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine

internal object DownloadPdfButton {

    private const val FILE_PDF_NAME = "todos.pdf"
    private const val OPEN_PDF_IN_NEW_TAB = "_blank"
    private const val TEMPLATE_TAGRET_PATH = "pdf/todos.html"
    private const val DOWNLOAD_BUTTON_TEXT = "Export selected to PDF"

    fun create(
        grid: Grid<Todo>,
        aggregateQueryPort: AggregateQueryPort,
        springTemplateEngine: SpringTemplateEngine
    ): Anchor =
        Anchor(StreamResource(FILE_PDF_NAME, getInputStreamPdf(grid, aggregateQueryPort, springTemplateEngine)), null)
            .apply { setTarget(OPEN_PDF_IN_NEW_TAB) }
            .also { downloadPdf ->
                Button(DOWNLOAD_BUTTON_TEXT)
                    .apply { addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL) }
                    .also { downloadPdf.add(it) }
            }

    private fun getInputStreamPdf(
        grid: Grid<Todo>,
        aggregateQueryPort: AggregateQueryPort,
        springTemplateEngine: SpringTemplateEngine
    ): () -> ByteArrayInputStream = {
        Context()
            .apply { setVariables(aggregateQueryPort.getComponentsToShowInPdf(grid.selectedItems)) }
            .let { springTemplateEngine.process(TEMPLATE_TAGRET_PATH, it) }
            .let { aggregateQueryPort.createPdfAsStream(it) }
    }
}
