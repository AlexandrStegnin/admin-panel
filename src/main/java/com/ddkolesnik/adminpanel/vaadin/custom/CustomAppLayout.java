package com.ddkolesnik.adminpanel.vaadin.custom;

import com.ddkolesnik.adminpanel.configuration.security.SecurityUtils;
import com.ddkolesnik.adminpanel.configuration.support.Constant;
import com.ddkolesnik.adminpanel.model.User;
import com.ddkolesnik.adminpanel.repository.AuthRepository;
import com.ddkolesnik.adminpanel.service.UserService;
import com.ddkolesnik.adminpanel.vaadin.ui.LoginView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.dom.Element;
import org.springframework.beans.factory.annotation.Autowired;


public class CustomAppLayout extends AppLayout {

    @Autowired
    private AuthRepository auth;

    private UserService userService;

    private AppLayoutMenu menu;

    private User currentDbUser;

    private void init() {
        menu = createMenu();
        menu.getElement().getStyle().set("padding", "10px");
        Image img = new Image("images/logo.jpg", "ToDo List Logo");
        img.setHeight("35px");
        setBranding(img);

        AppLayoutMenuItem logoutItem = new AppLayoutMenuItem(VaadinIcon.SIGN_OUT.create(), "ВЫЙТИ", e -> logout());
        AppLayoutMenuItem loginItem = new AppLayoutMenuItem(VaadinIcon.SIGN_IN.create(), "ВОЙТИ", e -> goToPage(LoginView.class));
        if (SecurityUtils.isUserLoggedIn()) {
            menu.addMenuItem(logoutItem);
        } else {
            menu.addMenuItem(loginItem);
        }

        menu.getElement().getChildren().forEach(this::stylizeItem);

    }

    private void stylizeItem(Element item) {
        item.getStyle().set("font-size", "16px");
        item.getStyle().set("font-weight", "bold");
        item.getStyle().set("color", "#ac1455");
    }

    public CustomAppLayout(UserService userService) {
        this.userService = userService;
        this.currentDbUser = userService.findByLogin(SecurityUtils.getUsername());
        init();
    }

    private void logout() {
        Notification.show("ВЫ УСПЕШНО ВЫШЛИ ИЗ СИСТЕМЫ!", 3000, Notification.Position.TOP_END);
        this.getUI().ifPresent(ui -> ui.navigate(Constant.LOGIN_PAGE));
        auth.logout();
    }

    private void goToPage(Class<? extends Component> clazz) {
        getUI().ifPresent(ui -> ui.navigate(clazz));
    }

    protected void reload() {
        menu.clearMenuItems();
        init();
    }

    protected User getCurrentDbUser() {
        return currentDbUser;
    }

}
