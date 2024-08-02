package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.dialog

import com.almland.vaadinplayground.application.port.inbound.AggregateCommandPort
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.dialog.button.AddButton
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.dialog.button.CancelButton
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.mapper.UIMapper
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField

internal object AddDialog {

    private const val DIALOG_HEADER_TITLE = "New todo"
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
                VerticalLayout()
                    .apply { add(title) }
                    .also { dialogLayout -> dialog.add(dialogLayout) }
                AddButton
                    .create(dialog, title, userName, uiMapper, aggregateCommandPort)
                    .also { addButton -> dialog.footer.add(addButton) }
                CancelButton
                    .create(dialog)
                    .also { cancelButton -> dialog.footer.add(cancelButton) }
            }
}