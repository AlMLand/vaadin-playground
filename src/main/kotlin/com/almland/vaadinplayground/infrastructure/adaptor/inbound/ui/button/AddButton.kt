package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.button

import com.almland.vaadinplayground.application.port.inbound.AggregateCommandPort
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.dialog.AddDialog
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.mapper.UIMapper
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant

internal object AddButton {

    private const val ADD_BUTTON_TEXT = "Add new"

    fun create(
        userName: String,
        uiMapper: UIMapper,
        aggregateCommandPort: AggregateCommandPort
    ): Button =
        Button(ADD_BUTTON_TEXT).apply {
            addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL)
            addClickListener { AddDialog.createAddDialog(userName, uiMapper, aggregateCommandPort).open() }
        }
}
