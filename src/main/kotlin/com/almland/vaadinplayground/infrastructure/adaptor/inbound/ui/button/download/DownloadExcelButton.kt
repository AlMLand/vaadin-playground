package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.button.download

import com.almland.vaadinplayground.application.port.inbound.AggregateQueryPort
import com.almland.vaadinplayground.domain.Todo
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.server.StreamResource
import java.io.ByteArrayInputStream

internal object DownloadExcelButton {

    private const val FILE_XLSX_NAME = "todos.xlsx"
    private const val DOWNLOAD_BUTTON_TEXT = "Export selected to Excel"

    fun create(grid: Grid<Todo>, aggregateQueryPort: AggregateQueryPort): Anchor =
        Anchor(StreamResource(FILE_XLSX_NAME, getInputStreamExcel(grid, aggregateQueryPort)), null)
            .also { anchor ->
                Button(DOWNLOAD_BUTTON_TEXT)
                    .apply { addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL) }
                    .also { anchor.add(it) }
            }

    private fun getInputStreamExcel(
        grid: Grid<Todo>,
        aggregateQueryPort: AggregateQueryPort
    ): () -> ByteArrayInputStream =
        { aggregateQueryPort.createExcelAsStream(grid.selectedItems) }
}
