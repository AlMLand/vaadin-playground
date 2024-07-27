package com.almland.vaadinplayground.todo

import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route

@Route("timur")
internal class TodoUI : VerticalLayout() {

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        val titel = H2("Hallo Timur!")
        add(titel)
    }
}
