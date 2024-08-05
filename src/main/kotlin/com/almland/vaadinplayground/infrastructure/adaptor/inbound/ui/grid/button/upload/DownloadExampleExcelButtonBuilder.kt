package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button.upload

import com.almland.vaadinplayground.application.port.inbound.AggregateQueryPort
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.server.StreamResource
import java.io.ByteArrayInputStream

internal object DownloadExampleExcelButtonBuilder {

    private const val FILE_XLSX_NAME = "todos-example.xlsx"
    private const val DOWNLOAD_BUTTON_TEXT = "Export example Excel"

    fun create(aggregateQueryPort: AggregateQueryPort): Anchor =
        Anchor(StreamResource(FILE_XLSX_NAME, getExcelAsStream(aggregateQueryPort)), null)
            .also { anchor ->
                Button(DOWNLOAD_BUTTON_TEXT, Icon(VaadinIcon.DOWNLOAD))
                    .apply { addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL) }
                    .also { anchor.add(it) }
            }

    private fun getExcelAsStream(aggregateQueryPort: AggregateQueryPort): () -> ByteArrayInputStream =
        { aggregateQueryPort.getExcelAsStream() }
}
