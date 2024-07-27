package com.almland.vaadinplayground.todo

import com.almland.vaadinplayground.todo.domain.Todo
import com.almland.vaadinplayground.todo.repostirory.InMemoryRepository
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.Key.ENTER
import com.vaadin.flow.component.Key.EXIT
import com.vaadin.flow.component.Key.KEY_A
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant.LUMO_SMALL
import com.vaadin.flow.component.button.ButtonVariant.LUMO_SUCCESS
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route

private const val USER_NAME_PARAMETER = "name"

@Route("todos/:$USER_NAME_PARAMETER")
internal class TodoUI(private val inMemoryRepository: InMemoryRepository) : VerticalLayout(), BeforeEnterObserver {

    private lateinit var userName: String
    private lateinit var view: Grid<Todo>

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)

        H2("Todo application: $userName").also { title -> add(title) }

        Button("Add new")
            .apply {
                addThemeVariants(LUMO_SUCCESS, LUMO_SMALL)
                addClickListener { createAddDialog().open() }
            }
            .also { button -> add(HorizontalLayout(button)) }

        view = Grid(Todo::class.java)
        refreshTodos()
        add(view)
    }

    private fun refreshTodos() {
        view.setItems(inMemoryRepository.getTodos())
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
                        addFocusShortcut(ENTER)
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
}
