package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.dialog

import com.almland.vaadinplayground.application.port.inbound.AggregateCommandPort
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.dialog.button.AddButtonBuilder
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.dialog.button.CancelButtonBuilder
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.mapper.UIMapper
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField

internal object AddDialogBuilder {

    private const val DIALOG_HEADER_TITLE = "New todo"
    private const val TEXT_FIELD_BODY = "Body"
    private const val TEXT_FIELD_TITLE = "Title"

    fun createAddDialog(
        userName: String,
        uiMapper: UIMapper,
        aggregateCommandPort: AggregateCommandPort
    ): Dialog =
        Dialog()
            .apply { headerTitle = DIALOG_HEADER_TITLE }
            .also { dialog ->
                val title = TextField(TEXT_FIELD_TITLE)
                    .apply { focus() }
                val body = TextField(TEXT_FIELD_BODY)
                VerticalLayout()
                    .apply { add(title, body) }
                    .also { dialog.add(it) }
                AddButtonBuilder
                    .create(dialog, title, body, userName, uiMapper, aggregateCommandPort)
                    .also { dialog.footer.add(it) }
                CancelButtonBuilder
                    .create(dialog)
                    .also { dialog.footer.add(it) }
            }
}
