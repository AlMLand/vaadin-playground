package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button.upload

import com.almland.vaadinplayground.application.port.inbound.AggregateCommandPort
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.synchroniseuibychanges.BroadcasterBuilder.broadcast
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MemoryBuffer

internal object UploadExcelButtonBuilder {

    fun create(userName: String, aggregateCommandPort: AggregateCommandPort): Upload =
        MemoryBuffer().let { memoryBuffer ->
            Upload(memoryBuffer).apply {
                addSucceededListener {
                    aggregateCommandPort.createFromStream(memoryBuffer.inputStream)
                    broadcast("New file with name: ${it.fileName} uploaded by $userName")
                }
            }
        }
}
