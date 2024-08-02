package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid

import com.almland.vaadinplayground.domain.Todo
import com.vaadin.flow.component.grid.Grid

internal object GridCreator {
    fun createTodoGrid(): Grid<Todo> =
        Grid(Todo::class.java, false)
            .apply {
                addColumn(Todo::title).setHeader(GridColumn.TITEL.name)
                addColumn(Todo::body).setHeader(GridColumn.BODY.name)
                addColumn(Todo::author).setHeader(GridColumn.AUTHOR.name)
                addColumn(Todo::createdAt).setHeader(GridColumn.CREATED.name)
                setSelectionMode(Grid.SelectionMode.MULTI)
            }
}
