package com.almland.vaadinplayground.todo

import com.almland.vaadinplayground.todo.domain.Todo
import com.almland.vaadinplayground.todo.repostirory.InMemoryRepository
import com.almland.vaadinplayground.todo.service.export.ExcelConverter
import com.almland.vaadinplayground.todo.service.synchroniseuibychanges.Broadcaster.broadcast
import com.almland.vaadinplayground.todo.service.synchroniseuibychanges.Broadcaster.register
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.DetachEvent
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.StreamResource
import com.vaadin.flow.shared.Registration
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

private const val USER_NAME_PARAMETER = "name"

@Route("todos/:$USER_NAME_PARAMETER")
internal class TodoUI(
    private val excelConverter: ExcelConverter,
    private val inMemoryRepository: InMemoryRepository
) : VerticalLayout(), BeforeEnterObserver, HasDynamicTitle {

    private lateinit var userName: String
    private lateinit var view: Grid<Todo>
    private lateinit var broadcastRegistration: Registration

    override fun onAttach(attachEvent: AttachEvent) {
        super.onAttach(attachEvent)

        H2("Todo application: $userName").also { title -> add(title) }

        HorizontalLayout().also { horizontalLayout ->
            Button("Add new").apply {
                addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL)
                addClickListener { createAddDialog().open() }
            }.also { horizontalLayout.add(it) }

            Button("Remove").apply {
                isVisible = userName.startsWith('a')
                addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL)
                addClickListener {
                    inMemoryRepository.removeTodos(view.selectedItems)
                    broadcast("Todo(s) removed by $userName")
                }
            }.also { horizontalLayout.add(it) }

            Anchor(
                StreamResource("todos.xlsx", getInputStream()),
                null
            ).also { downloadExcel ->
                Button("Export selected to Excel")
                    .apply { addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL) }
                    .also { downloadExcel.add(it) }
                horizontalLayout.add(downloadExcel)
            }

            add(horizontalLayout)
        }

        Grid(Todo::class.java)
            .apply {
                isAllRowsVisible = true
                setSelectionMode(Grid.SelectionMode.MULTI)
            }
            .also {
                view = it
                refreshTodos()
                add(it)
            }

        HorizontalLayout().also { horizontalLayout ->
            Button("Select all").apply {
                addThemeVariants(ButtonVariant.LUMO_TERTIARY)
                icon = VaadinIcon.PLUS.create()
                addClickListener { view.asMultiSelect().select(inMemoryRepository.getTodos()) }
            }.also { horizontalLayout.add(it) }
            Button("Deselect all").apply {
                addThemeVariants(ButtonVariant.LUMO_TERTIARY)
                icon = VaadinIcon.MINUS.create()
                addClickListener { view.asMultiSelect().deselectAll() }
            }.also { horizontalLayout.add(it) }
            add(horizontalLayout)
        }

        attachEvent.ui.also { ui ->
            broadcastRegistration = register { message ->
                ui.access {
                    refreshTodos()
                    Notification.show(message, 2000, Notification.Position.BOTTOM_CENTER)
                }
            }
        }
    }

    private fun getInputStream(): () -> ByteArrayInputStream = {
        ByteArrayOutputStream()
            .apply { excelConverter.createExcelFile(view.selectedItems).write(this) }
            .let { ByteArrayInputStream(it.toByteArray()) }
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
                        addFocusShortcut(Key.ENTER)
                        addClickListener {
                            inMemoryRepository.addTodo(Todo(title.value, title.value, userName))
                            dialog.close()
                            broadcast("Todo added by $userName")
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

    override fun onDetach(detachEvent: DetachEvent) {
        super.onDetach(detachEvent)
        broadcastRegistration.remove()
    }
}
