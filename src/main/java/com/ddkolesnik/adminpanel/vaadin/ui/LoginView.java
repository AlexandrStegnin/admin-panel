package com.ddkolesnik.adminpanel.vaadin.ui;

import com.ddkolesnik.adminpanel.repository.AuthRepository;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import static com.ddkolesnik.adminpanel.configuration.support.Constant.APP_TOKEN_PAGE;
import static com.ddkolesnik.adminpanel.configuration.support.Location.LOGIN_PAGE;

/**
 * @author Alexandr Stegnin
 */

@PageTitle("СТРАНИЦА ВХОДА")
@Route(value = LOGIN_PAGE, layout = MainLayout.class)
//@HtmlImport("../VAADIN/shared-styles.html")
public class LoginView extends Div {

    private final AuthRepository authRepository;

    public LoginView(AuthRepository authRepository) {
        this.authRepository = authRepository;
        init();
    }

    private void init() {
        createSignInForm();
    }

    private boolean authenticated(String login, String password) {
        return authRepository.authenticate(login, password).isAuthenticated();
    }

    private void createSignInForm() {
        Div loginPage = new Div();
        loginPage.addClassNames("login-page", "ls-closed");
        Div loginBox = new Div();
        loginBox.addClassName("login-box");
        loginPage.add(loginBox);

        Div logo = new Div();
        logo.addClassName("logo");
        Html link = new Html("<a href=\"javascript:void(0);\">TODO LIST</a>");

        Html smallHtml = new Html("<small>ПОМОГАЕМ РЕШАТЬ ЛЮБЫЕ ЗАДАЧИ</small>");
        logo.add(link, smallHtml);
        loginBox.add(logo);

        Div card = new Div();
        card.addClassName("card");
        loginBox.add(card);

        Div body = new Div();
        body.addClassName("body");
        card.add(body);

        body.add(createForm());
        add(loginPage);
    }

    private Div createForm() {

        Div form = new Div();
        Div msg = new Div();
        msg.addClassName("msg");
        msg.setText(" ");
        form.add(msg);

        Div usernameGroupDiv = new Div();
        usernameGroupDiv.addClassName("input-group");
        Span inputGroupAddon = new Span();
        inputGroupAddon.addClassName("input-group-addon");
        Html personIcon = new Html("<i class=\"material-icons\">person</i>");
        inputGroupAddon.add(personIcon);
        usernameGroupDiv.add(inputGroupAddon);

        Div usernameInline = new Div();
        usernameInline.addClassName("form-line");
        usernameGroupDiv.add(usernameInline);

        Input loginInput = new Input();
        loginInput.setType("text");
        loginInput.setPlaceholder("ИМЯ ПОЛЬЗОВАТЕЛЯ");
        loginInput.addClassName("form-control");
        loginInput.isRequiredIndicatorVisible();

        usernameInline.add(loginInput);

        form.add(usernameGroupDiv);

        Div passwordGroupDiv = new Div();
        passwordGroupDiv.addClassName("input-group");
        Span inputGroupAddonPass = new Span();
        inputGroupAddonPass.addClassName("input-group-addon");
        Html lockIcon = new Html("<i class=\"material-icons\">lock</i>");
        inputGroupAddonPass.add(lockIcon);
        passwordGroupDiv.add(inputGroupAddonPass);

        Div passwordInline = new Div();
        passwordInline.addClassName("form-line");
        passwordGroupDiv.add(passwordInline);

        Input passwordInput = new Input();
        passwordInput.setType("password");
        passwordInput.setPlaceholder("ПАРОЛЬ");
        passwordInput.addClassName("form-control");
        passwordInput.isRequiredIndicatorVisible();

        passwordInline.add(passwordInput);

        form.add(passwordGroupDiv);

        Div rowSubmit = new Div();
        rowSubmit.addClassName("row");
        form.add(rowSubmit);

        Div colSubmit = new Div();
        colSubmit.addClassNames("col-xs-offset-8", "col-xs-4");
        rowSubmit.add(colSubmit);

        Button signIn = new Button("ВОЙТИ", e -> {
            if (authenticated(loginInput.getValue(), passwordInput.getValue())) {
                this.getUI().ifPresent(ui -> ui.navigate(APP_TOKEN_PAGE));
            }
        });

        signIn.addClassNames("btn", "btn-block", "bg-pink", "waves-effect");
        signIn.getStyle().set("padding", "8px 0 25px 0");
        colSubmit.add(signIn);

        return form;
    }

}
