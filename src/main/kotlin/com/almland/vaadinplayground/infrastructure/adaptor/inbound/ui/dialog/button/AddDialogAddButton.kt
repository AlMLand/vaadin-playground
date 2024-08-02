package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.dialog.button

import com.almland.vaadinplayground.application.port.inbound.AggregateCommandPort
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.mapper.UIMapper
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.synchroniseuibychanges.Broadcaster.broadcast
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.textfield.TextField

internal object AddDialogAddButton {

    private const val ADD_BUTTON_TEXT = "Add"

    fun create(
        dialog: Dialog,
        title: TextField,
        userName: String,
        uiMapper: UIMapper,
        aggregateCommandPort: AggregateCommandPort
    ): Button =
        Button(ADD_BUTTON_TEXT)
            .apply {
                addFocusShortcut(Key.ENTER)
                addClickListener {
                    uiMapper
                        .mapToTodo(title.value, title.value, userName)
                        .also { aggregateCommandPort.create(it) }
                    dialog.close()
                    broadcast("Todo added by $userName")
                }
            }
}