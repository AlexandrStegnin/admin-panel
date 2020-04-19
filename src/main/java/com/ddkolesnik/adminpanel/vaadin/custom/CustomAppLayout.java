package com.ddkolesnik.adminpanel.vaadin.custom;

import com.ddkolesnik.adminpanel.configuration.security.SecurityUtils;
import com.ddkolesnik.adminpanel.model.User;
import com.ddkolesnik.adminpanel.repository.AuthRepository;
import com.ddkolesnik.adminpanel.service.UserService;
import com.ddkolesnik.adminpanel.vaadin.support.VaadinViewUtils;
import com.ddkolesnik.adminpanel.vaadin.ui.AppTokenView;
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

import static com.ddkolesnik.adminpanel.configuration.support.Location.LOGIN_PAGE;


public class CustomAppLayout extends AppLayout {

    @Autowired
    private AuthRepository auth;

    private final UserService userService;

    private AppLayoutMenu menu;

    private final User currentDbUser;

    private void init() {
        menu = createMenu();
        menu.getElement().getStyle().set("padding", "10px");
        Image img = VaadinViewUtils.getLogo(51);
        menu.addMenuItem(img);
        AppLayoutMenuItem logoutItem = new AppLayoutMenuItem(VaadinIcon.SIGN_OUT.create(), "ВЫЙТИ", e -> logout());
        AppLayoutMenuItem loginItem = new AppLayoutMenuItem(VaadinIcon.SIGN_IN.create(), "ВОЙТИ", e -> goToPage(LoginView.class));
        AppLayoutMenuItem usersItem = new AppLayoutMenuItem(VaadinIcon.USER.create(), "ПОЛЬЗОВАТЕЛИ", e -> goToPage(LoginView.class));
        AppLayoutMenuItem rolesItem = new AppLayoutMenuItem(VaadinIcon.SHIELD.create(), "РОЛИ", e -> goToPage(LoginView.class));
        AppLayoutMenuItem tokensItem = new AppLayoutMenuItem(VaadinIcon.LOCK.create(), "ТОКЕНЫ", e -> goToPage(AppTokenView.class));
        if (SecurityUtils.isUserLoggedIn()) {
            menu.addMenuItem(usersItem);
            menu.addMenuItem(rolesItem);
            menu.addMenuItem(tokensItem);
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
        this.getUI().ifPresent(ui -> ui.navigate(LOGIN_PAGE));
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
