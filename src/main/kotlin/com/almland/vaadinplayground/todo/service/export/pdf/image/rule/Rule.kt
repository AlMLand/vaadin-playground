package com.almland.vaadinplayground.todo.service.export.pdf.image.rule

import java.util.function.Supplier

internal data class Rule(
    val condition: Supplier<Boolean>,
    val process: Supplier<String>
)
