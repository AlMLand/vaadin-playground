package com.almland.vaadinplayground.infrastructure.adaptor.inbound.ui.layout

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.Scroller
import com.vaadin.flow.component.sidenav.SideNav
import com.vaadin.flow.component.sidenav.SideNavItem
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.lumo.LumoUtility

@Route("app")
@PageTitle("app")
internal class CurrentAppLayout : AppLayout() {

    companion object {
        private const val APP_MAIN_HEADER = "MyApp"
        private const val PLACEHOLDER_ONLY_FOR_TEST_PURPOSE = "alex"
    }

    init {
        getScroller(getSideNav()).also { addToDrawer(it) }
        addToNavbar(DrawerToggle(), getHeader())
        setPrimarySection(Section.DRAWER)
    }

    private fun getHeader(): H1 =
        H1(APP_MAIN_HEADER)
            .apply {
                style
                    .setMargin("0")
                    .setFontSize("var(--lumo-font-size-l)")
            }

    private fun getScroller(sideNav: SideNav): Scroller =
        Scroller(sideNav)
            .apply { className = LumoUtility.Padding.SMALL }

    private fun getSideNav(): SideNav =
        SideNav()
            .apply {
                addItem(
                    SideNavItem("To start", "/app", VaadinIcon.STAR.create()),
                    SideNavItem("Todo", "/todos/$PLACEHOLDER_ONLY_FOR_TEST_PURPOSE", VaadinIcon.LIST.create()),
                    SideNavItem("Vaadin website", "https://vaadin.com", VaadinIcon.VAADIN_H.create())
                        .apply { isOpenInNewBrowserTab = true }
                )
            }
}
