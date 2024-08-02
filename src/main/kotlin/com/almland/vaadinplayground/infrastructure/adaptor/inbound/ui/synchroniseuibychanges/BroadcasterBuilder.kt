package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.synchroniseuibychanges

import com.vaadin.flow.shared.Registration
import java.util.LinkedList
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.function.Consumer

internal object BroadcasterBuilder {

    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val listeners: MutableCollection<Consumer<String>> = LinkedList()

    @Synchronized
    fun register(consumer: Consumer<String>): Registration {
        listeners.add(consumer)
        return Registration { synchronized(BroadcasterBuilder::class.java) { listeners.remove(consumer) } }
    }

    @Synchronized
    fun broadcast(message: String) {
        listeners
            .forEach { listener -> executor.execute { listener.accept(message) } }
    }
}
