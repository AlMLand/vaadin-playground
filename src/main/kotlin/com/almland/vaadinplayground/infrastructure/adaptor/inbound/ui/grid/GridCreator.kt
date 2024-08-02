package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid

import com.vaadin.flow.component.grid.Grid

internal object GridCreator {
    fun <T> createGrid(clazz: Class<T>): Grid<T> =
        Grid(clazz)
            .apply {
                isAllRowsVisible = true
                setSelectionMode(Grid.SelectionMode.MULTI)
            }
}
