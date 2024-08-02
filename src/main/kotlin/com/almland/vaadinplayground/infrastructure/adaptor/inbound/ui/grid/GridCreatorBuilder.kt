package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid

import com.almland.vaadinplayground.domain.Todo
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid


internal object GridCreatorBuilder {

    fun createTodoGrid(showHideColumnButton: Button): Grid<Todo> =
        Grid(Todo::class.java, false)
            .apply {
                ColumnToggleContextMenu(showHideColumnButton).also { menu ->
                    addColumn(Todo::title).setHeader(GridColumn.TITEL.name)
                        .also { menu.addColumnToggleItem(GridColumn.TITEL.name, it) }
                    addColumn(Todo::body).setHeader(GridColumn.BODY.name)
                        .also { menu.addColumnToggleItem(GridColumn.BODY.name, it) }
                    addColumn(Todo::author).setHeader(GridColumn.AUTHOR.name)
                        .also { menu.addColumnToggleItem(GridColumn.AUTHOR.name, it) }
                    addColumn(Todo::createdAt).setHeader(GridColumn.CREATED.name)
                        .also { menu.addColumnToggleItem(GridColumn.CREATED.name, it) }
                    setSelectionMode(Grid.SelectionMode.MULTI)
                }
            }
}
