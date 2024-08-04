package com.almland.vaadinplayground.infrastructure.configuration

import com.almland.vaadinplayground.application.aggregate.TodoAggregate
import com.almland.vaadinplayground.application.port.outbound.PersistenceCommandPort
import com.almland.vaadinplayground.application.port.outbound.PersistenceQueryPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class TodoAggregateConfiguration {
    @Bean
    fun todoAggregate(
        persistenceQueryPort: PersistenceQueryPort,
        persistenceCommandPort: PersistenceCommandPort
    ): TodoAggregate = TodoAggregate(persistenceQueryPort, persistenceCommandPort)
}