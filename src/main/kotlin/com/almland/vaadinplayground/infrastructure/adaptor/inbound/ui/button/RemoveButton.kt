package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.button

import com.almland.vaadinplayground.application.port.inbound.AggregateCommandPort
import com.almland.vaadinplayground.domain.Todo
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.synchroniseuibychanges.Broadcaster.broadcast
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid

internal object RemoveButton {

    private const val VERIFY_CONDITION = 'a'
    private const val REMOVE_BUTTON_TEXT = "Remove"

    fun create(
        grid: Grid<Todo>,
        userName: String,
        aggregateCommandPort: AggregateCommandPort
    ): Button =
        Button(REMOVE_BUTTON_TEXT).apply {
            isVisible = userName.startsWith(VERIFY_CONDITION)
            addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL)
            addClickListener {
                aggregateCommandPort.deleteAll(grid.selectedItems.map { it.id })
                broadcast("Todo(s) removed by $userName")
            }
        }
}
