package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui

import com.almland.vaadinplayground.application.port.inbound.AggregateCommandPort
import com.almland.vaadinplayground.application.port.inbound.AggregateQueryPort
import com.almland.vaadinplayground.application.port.inbound.UIPort
import com.almland.vaadinplayground.domain.Todo
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.GridCreatorBuilder
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button.AddButtonBuilder
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button.RemoveButtonBuilder
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button.download.DownloadExcelButtonBuilder
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button.download.DownloadPdfButtonBuilder
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button.table.DeselectAllButtonBuilder
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button.table.SelectAllButtonBuilder
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button.table.ShowHideColumnButtonBuilder
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.grid.button.upload.UploadExcelButtonBuilder
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.mapper.UIMapper
import com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.synchroniseuibychanges.BroadcasterBuilder.register
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.DetachEvent
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.shared.Registration
import org.thymeleaf.spring6.SpringTemplateEngine

private const val USER_NAME_URL_PARAMETER = "name"
private const val APPLICATION_START_TITLE = "Todo application:"
private const val EVENT_NOT_AVAILABLE_DEFAULT = "No user name available"

@Route("todos/:$USER_NAME_URL_PARAMETER")
internal class UIAdaptor(
    private val uiMapper: UIMapper,
    private val aggregateQueryPort: AggregateQueryPort,
    private val aggregateCommandPort: AggregateCommandPort,
    private val springTemplateEngine: SpringTemplateEngine
) : VerticalLayout(), BeforeEnterObserver, HasDynamicTitle, UIPort {

    private lateinit var userName: String
    private lateinit var broadcastRegistration: Registration
    private val showHideColumnButton = ShowHideColumnButtonBuilder.create()
    private var grid: Grid<Todo> = GridCreatorBuilder.createTodoGrid(showHideColumnButton)

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)

        H2("$APPLICATION_START_TITLE $userName").also { title -> add(title) }

        HorizontalLayout().also { horizontalLayout ->
            horizontalLayout.setWidthFull()

            AddButtonBuilder
                .create(userName, uiMapper, aggregateCommandPort)
                .also { horizontalLayout.add(it) }

            RemoveButtonBuilder
                .create(grid, userName, aggregateCommandPort)
                .also { horizontalLayout.add(it) }

            DownloadExcelButtonBuilder
                .create(grid, aggregateQueryPort)
                .also { horizontalLayout.add(it) }

            DownloadPdfButtonBuilder
                .create(grid, aggregateQueryPort, springTemplateEngine)
                .also { horizontalLayout.add(it) }

            horizontalLayout.add(showHideColumnButton)
            add(horizontalLayout)
        }

        grid.also { refresh();add(it) }

        HorizontalLayout().also { horizontalLayout ->
            SelectAllButtonBuilder
                .create(grid, aggregateQueryPort)
                .also { horizontalLayout.add(it) }

            DeselectAllButtonBuilder
                .create(grid)
                .also { horizontalLayout.add(it) }

            add(horizontalLayout)
        }

        UploadExcelButtonBuilder
            .create(userName, aggregateCommandPort)
            .also { add(it) }

        attachEvent?.ui.also { ui ->
            broadcastRegistration = register { message ->
                ui?.access {
                    refresh()
                    Notification.show(message, 2000, Notification.Position.BOTTOM_CENTER)
                }
            }
        }
    }

    override fun refresh() {
        grid.setItems(aggregateQueryPort.getAll())
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
