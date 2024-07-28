package com.almland.vaadinplayground.todo

import com.almland.vaadinplayground.todo.domain.Todo
import com.almland.vaadinplayground.todo.repostirory.InMemoryRepository
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.Route

private const val USER_NAME_PARAMETER = "name"

@Route("todos/:$USER_NAME_PARAMETER")
internal class TodoUI(
    private val inMemoryRepository: InMemoryRepository
) : VerticalLayout(), BeforeEnterObserver, HasDynamicTitle {

    private lateinit var userName: String
    private lateinit var view: Grid<Todo>

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)

        H2("Todo application: $userName").also { title -> add(title) }

        HorizontalLayout().also { horizontalLayout ->
            Button("Add new").apply {
                addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL)
                addClickListener { createAddDialog().open() }
            }.also { horizontalLayout.add(it) }
            Button("Remove").apply {
                addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL)
                addClickListener {
                    inMemoryRepository.removeTodos(view.selectedItems)
                    refreshTodos()
                }
            }.also { horizontalLayout.add(it) }
            add(horizontalLayout)
        }

        view = Grid(Todo::class.java)
            .apply { setSelectionMode(Grid.SelectionMode.MULTI) }
            .also {
                refreshTodos(it)
                add(it)
            }
    }

    private fun refreshTodos(view: Grid<Todo>? = null) {
        (view.takeIf { it != null } ?: this.view)
            .setItems(inMemoryRepository.getTodos())
    }

    private fun createAddDialog(): Dialog =
        Dialog()
            .apply { headerTitle = "New todo" }
            .also { dialog ->
                val title = TextField("Title")
                    .apply { focus() }
                VerticalLayout()
                    .apply { add(title) }
                    .also { dialogLayout -> dialog.add(dialogLayout) }
                Button("Add")
                    .apply {
                        addFocusShortcut(Key.ENTER)
                        addClickListener {
                            inMemoryRepository.addTodo(Todo(title.value, title.value, userName))
                            dialog.close()
                            refreshTodos()
                        }
                    }
                    .also { addButton -> dialog.footer.add(addButton) }
                Button("Cancel")
                    .apply { addClickListener { dialog.close() } }
                    .also { cancelButton -> dialog.footer.add(cancelButton) }
            }

    override fun beforeEnter(event: BeforeEnterEvent?) {
        userName = event
            ?.let {
                it
                    .routeParameters[USER_NAME_PARAMETER]
                    .orElseThrow { RuntimeException("route parameter not contains the parameter $USER_NAME_PARAMETER") }
            }
            ?: "no user name available"
    }

    override fun getPageTitle(): String = "todo: $userName"
}
