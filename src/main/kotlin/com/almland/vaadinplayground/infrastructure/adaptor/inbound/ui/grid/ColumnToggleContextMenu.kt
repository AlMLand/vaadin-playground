package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid

import com.almland.vaadinplayground.domain.Todo
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.contextmenu.ContextMenu
import com.vaadin.flow.component.contextmenu.MenuItem
import com.vaadin.flow.component.grid.Grid

internal class ColumnToggleContextMenu(target: Component) : ContextMenu(target) {

    init {
        isOpenOnClick = true
    }

    fun addColumnToggleItem(label: String, column: Grid.Column<Todo>) {
        (addItem(label) { e: ClickEvent<MenuItem> -> column.setVisible(e.source.isChecked) })
            .let {
                it.isCheckable = true
                it.isChecked = column.isVisible
                it.isKeepOpen = true
            }
    }
}
