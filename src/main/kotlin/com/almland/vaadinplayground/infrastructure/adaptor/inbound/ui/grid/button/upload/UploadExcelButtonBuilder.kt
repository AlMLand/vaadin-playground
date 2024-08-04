package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button.upload

import com.almland.vaadinplayground.application.port.inbound.AggregateCommandPort
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.synchroniseuibychanges.BroadcasterBuilder.broadcast
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MemoryBuffer

internal object UploadExcelButtonBuilder {

    private const val UPLOAD_BUTTON_TEXT = "Upload Excel"
    private val acceptedFileType = arrayOf("application/xlsx", ".xlsx")

    fun create(userName: String, aggregateCommandPort: AggregateCommandPort): Upload =
        MemoryBuffer().let { memoryBuffer ->
            Upload(memoryBuffer).apply {
                Button(UPLOAD_BUTTON_TEXT, Icon(VaadinIcon.UPLOAD))
                    .apply { addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL) }
                    .also { uploadButton = it }
                setAcceptedFileTypes(*acceptedFileType)
                addSucceededListener {
                    aggregateCommandPort.createFromStream(memoryBuffer.inputStream)
                    broadcast("New file with name: ${it.fileName} uploaded by $userName")
                }
                addFileRejectedListener {
                    Notification
                        .show(it.errorMessage, 2000, Notification.Position.BOTTOM_CENTER)
                        .apply { addThemeVariants(NotificationVariant.LUMO_ERROR) }
                }
                addFailedListener {
                    Notification
                        .show(
                            "Internal exception: ${it.reason.message} is occurred, by upload from file: ${it.fileName}",
                            2000,
                            Notification.Position.BOTTOM_CENTER
                        )
                        .apply { addThemeVariants(NotificationVariant.LUMO_ERROR) }
                }
            }
        }
}
