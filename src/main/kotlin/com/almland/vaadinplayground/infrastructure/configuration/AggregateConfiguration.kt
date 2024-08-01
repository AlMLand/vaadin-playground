package com.almland.vaadinplayground.infrastructure.configuration

import com.almland.vaadinplayground.application.aggregate.Aggregate
import com.almland.vaadinplayground.application.port.outbound.PersistenceCommandPort
import com.almland.vaadinplayground.application.port.outbound.PersistenceQueryPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class AggregateConfiguration {
    @Bean
    fun aggregate(
        persistenceQueryPort: PersistenceQueryPort,
        persistenceCommandPort: PersistenceCommandPort
    ): Aggregate = Aggregate(persistenceQueryPort, persistenceCommandPort)
}