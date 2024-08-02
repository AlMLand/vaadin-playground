package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.dialog.button

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog

internal object CancelButtonBuilder {

    private const val CANCEL_BUTTON_TEXT = "Cancel"

    fun create(dialog: Dialog): Button =
        Button(CANCEL_BUTTON_TEXT)
            .apply { addClickListener { dialog.close() } }
}
