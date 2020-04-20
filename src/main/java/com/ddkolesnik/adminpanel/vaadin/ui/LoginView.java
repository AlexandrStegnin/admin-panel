package com.ddkolesnik.adminpanel.vaadin.ui;

import com.ddkolesnik.adminpanel.configuration.support.Internationalization;
import com.ddkolesnik.adminpanel.repository.AuthRepository;
import com.ddkolesnik.adminpanel.vaadin.support.VaadinViewUtils;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import static com.ddkolesnik.adminpanel.configuration.support.Location.ADMIN_PAGE;
import static com.ddkolesnik.adminpanel.configuration.support.Location.LOGIN_PAGE;

/**
 * @author Alexandr Stegnin
 */

@PageTitle("АВТОРИЗАЦИЯ")
@Route(value = LOGIN_PAGE, layout = MainLayout.class)
public class LoginView extends HorizontalLayout {

    private final AuthRepository authRepository;

    public LoginView(AuthRepository authRepository) {
        this.authRepository = authRepository;
        init();
    }

    private void init() {
        LoginOverlay loginForm = new LoginOverlay();
        Div title = new Div();

        title.add(VaadinViewUtils.getLogo(350));

        loginForm.setTitle(title);
        loginForm.setDescription("");
        loginForm.setOpened(true);
        loginForm.setI18n(Internationalization.russianI18n());
        loginForm.setForgotPasswordButtonVisible(false);
        loginForm.addLoginListener(e -> {
            boolean authenticated = authenticate(e);
            if (authenticated) {
                loginForm.close();
                getUI().ifPresent(ui -> ui.navigate(ADMIN_PAGE));
            } else {
                loginForm.setError(true);
            }
        });
        setAlignItems(Alignment.CENTER);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(loginForm);
    }

    private boolean authenticate(AbstractLogin.LoginEvent e) {
        return authRepository.authenticate(e.getUsername(), e.getPassword()).isAuthenticated();
    }

}
