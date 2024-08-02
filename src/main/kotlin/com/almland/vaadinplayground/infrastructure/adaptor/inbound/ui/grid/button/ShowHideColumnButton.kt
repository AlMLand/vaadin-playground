package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant

internal object ShowHideColumnButton {

    private const val SHOW_HIDE_COLUMNS_BUTTON_TEXT = "Show/Hide columns"

    fun create(): Button =
        Button(SHOW_HIDE_COLUMNS_BUTTON_TEXT)
            .apply {
                element.style.set("margin-left", "auto")
                addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL)
            }
}
