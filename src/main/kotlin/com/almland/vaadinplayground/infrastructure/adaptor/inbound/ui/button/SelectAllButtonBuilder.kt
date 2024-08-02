package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.button

import com.almland.vaadinplayground.application.port.inbound.AggregateQueryPort
import com.almland.vaadinplayground.domain.Todo
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon

internal object SelectAllButtonBuilder {

    private const val SELECT_ALL_BUTTON_TEXT = "Select all"

    fun create(
        grid: Grid<Todo>,
        aggregateQueryPort: AggregateQueryPort
    ): Button =
        Button(SELECT_ALL_BUTTON_TEXT).apply {
            addThemeVariants(ButtonVariant.LUMO_TERTIARY)
            icon = VaadinIcon.PLUS.create()
            addClickListener { grid.asMultiSelect().select(aggregateQueryPort.getAll()) }
        }
}
