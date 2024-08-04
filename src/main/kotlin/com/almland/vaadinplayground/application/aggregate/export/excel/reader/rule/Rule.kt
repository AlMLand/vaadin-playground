package com.almland.vaadinplayground.application.aggregate.export.excel.reader.rule

import java.util.function.Supplier

internal data class Rule(
    val condition: Supplier<Boolean>,
    val process: Supplier<Any>
)
