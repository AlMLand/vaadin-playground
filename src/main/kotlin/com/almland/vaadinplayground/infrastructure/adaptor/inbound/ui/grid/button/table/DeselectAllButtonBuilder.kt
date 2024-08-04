package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button.table

import com.almland.vaadinplayground.domain.Todo
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon

internal object DeselectAllButtonBuilder {

    private const val DESELECT_ALL_BUTTON_TEXT = "Deselect all"

    fun create(grid: Grid<Todo>): Button =
        Button(DESELECT_ALL_BUTTON_TEXT).apply {
            addThemeVariants(ButtonVariant.LUMO_TERTIARY)
            icon = VaadinIcon.MINUS.create()
            addClickListener { grid.asMultiSelect().deselectAll() }
        }
}
