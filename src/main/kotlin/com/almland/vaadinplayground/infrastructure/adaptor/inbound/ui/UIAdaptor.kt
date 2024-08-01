package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui

import com.almland.vaadinplayground.application.port.inbound.AggregateCommandPort
import com.almland.vaadinplayground.application.port.inbound.AggregateQueryPort
import com.almland.vaadinplayground.application.port.inbound.UIPort
import com.almland.vaadinplayground.domain.Todo
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.mapper.UIMapper
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.synchroniseuibychanges.Broadcaster.broadcast
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.synchroniseuibychanges.Broadcaster.register
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
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine

private const val OPEN_PDF_IN_NEW_TAB = "_blank"
private const val USER_NAME_URL_PARAMETER = "name"
private const val EVENT_NOT_AVAILABLE_DEFAULT = "No user name available"

@Route("todos/:$USER_NAME_URL_PARAMETER")
internal class UIAdaptor(
    private val uiMapper: UIMapper,
    private val aggregateQueryPort: AggregateQueryPort,
    private val aggregateCommandPort: AggregateCommandPort,
    private val springTemplateEngine: SpringTemplateEngine
) : VerticalLayout(), BeforeEnterObserver, HasDynamicTitle, UIPort {

    private lateinit var userName: String
    private lateinit var view: Grid<Todo>
    private lateinit var broadcastRegistration: Registration

    override fun onAttach(attachEvent: AttachEvent?) {
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
                    aggregateCommandPort.deleteAll(view.selectedItems.map { it.id })
                    broadcast("Todo(s) removed by $userName")
                }
            }.also { horizontalLayout.add(it) }

            Anchor(
                StreamResource("todos.xlsx", aggregateQueryPort.getInputStreamExcel(view.selectedItems)),
                null
            )
                .also { downloadExcel ->
                    Button("Export selected to Excel")
                        .apply { addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL) }
                        .also { downloadExcel.add(it) }
                    horizontalLayout.add(downloadExcel)
                }

            Anchor(StreamResource("todos.pdf", getInputStreamPdf(view.selectedItems)), null)
                .apply { setTarget(OPEN_PDF_IN_NEW_TAB) }
                .also { downloadPdf ->
                    Button("Export selected to PDF")
                        .apply { addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL) }
                        .also { downloadPdf.add(it) }
                    horizontalLayout.add(downloadPdf)
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
                addClickListener { view.asMultiSelect().select(aggregateQueryPort.getAll()) }
            }.also { horizontalLayout.add(it) }
            Button("Deselect all").apply {
                addThemeVariants(ButtonVariant.LUMO_TERTIARY)
                icon = VaadinIcon.MINUS.create()
                addClickListener { view.asMultiSelect().deselectAll() }
            }.also { horizontalLayout.add(it) }
            add(horizontalLayout)
        }

        attachEvent?.ui.also { ui ->
            broadcastRegistration = register { message ->
                ui?.access {
                    refreshTodos()
                    Notification.show(message, 2000, Notification.Position.BOTTOM_CENTER)
                }
            }
        }
    }

    private fun getInputStreamPdf(todos: Set<Todo>): () -> ByteArrayInputStream =
        Context()
            .apply { setVariables(aggregateQueryPort.getComponentsToShowInPdf(todos)) }
            .let { springTemplateEngine.process("pdf/todos.html", it) }
            .let { aggregateQueryPort.createPdf(it) }

    private fun refreshTodos() {
        view.setItems(aggregateQueryPort.getAll())
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
                            uiMapper
                                .mapToTodo(title.value, title.value, userName)
                                .also { aggregateCommandPort.save(it) }
                            dialog.close()
                            broadcast("Todo added by $userName")
                        }
                    }
                    .also { addButton -> dialog.footer.add(addButton) }
                Button("Cancel")
                    .apply { addClickListener { dialog.close() } }
                    .also { cancelButton -> dialog.footer.add(cancelButton) }
            }

    override fun onDetach(detachEvent: DetachEvent?) {
        super.onDetach(detachEvent)
        broadcastRegistration.remove()
    }

    override fun beforeEnter(event: BeforeEnterEvent?) {
        userName = event?.let {
            it
                .routeParameters[USER_NAME_URL_PARAMETER]
                .orElseThrow { RuntimeException("Url parameter not contains the parameter $USER_NAME_URL_PARAMETER") }
        } ?: EVENT_NOT_AVAILABLE_DEFAULT
    }

    override fun getPageTitle(): String = "Todo: $userName"
}
