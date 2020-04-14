package com.ddkolesnik.adminpanel.vaadin.form;

import com.ddkolesnik.adminpanel.command.Command;
import com.ddkolesnik.adminpanel.command.token.CreateTokenCommand;
import com.ddkolesnik.adminpanel.command.token.DeleteTokenCommand;
import com.ddkolesnik.adminpanel.command.token.UpdateTokenCommand;
import com.ddkolesnik.adminpanel.configuration.support.OperationEnum;
import com.ddkolesnik.adminpanel.model.AppToken;
import com.ddkolesnik.adminpanel.service.AppTokenService;
import com.ddkolesnik.adminpanel.vaadin.support.VaadinViewUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

import java.util.UUID;


/**
 * @author Alexandr Stegnin
 */

public class AppTokenForm extends Dialog {

    private final AppTokenService appTokenService;
    private final AppToken appToken;
    private final TextField appName;
    private final TextField token;
    private final Binder<AppToken> appTokenBinder;
    private final OperationEnum operation;
    private final Button cancel;
    private final HorizontalLayout buttons;
    private final VerticalLayout content;
    private Button submit;
    private boolean canceled = false;

    public AppTokenForm(OperationEnum operation, AppToken appToken, AppTokenService appTokenService) {
        this.appName = new TextField("НАЗВАНИЕ ПРИЛОЖЕНИЯ");
        this.token = new TextField("ТОКЕН");
        this.appTokenBinder = new BeanValidationBinder<>(AppToken.class);
        this.appTokenService = appTokenService;
        this.operation = operation;
        this.submit = VaadinViewUtils.createButton(
                operation.name.toUpperCase(), "", "submit", "8px 10px 19px 6px");
        this.cancel = VaadinViewUtils.createButton("ОТМЕНИТЬ", "", "cancel", "8px 10px 19px 6px");
        this.buttons = new HorizontalLayout();
        this.content = new VerticalLayout();
        this.appToken = appToken;
        init();
    }

    private void init() {
        prepareButtons(operation);
        stylizeForm();
        buttons.add(submit, cancel);
        content.add(appName, token, buttons);
        add(content);
        if (appToken.getToken() == null || appToken.getToken().isEmpty()) {
            this.appToken.setToken(generateToken());
        }
        appTokenBinder.setBean(appToken);
        appTokenBinder.bindInstanceFields(this);
    }

    private void executeCommand(Command command) {
        if (operation.compareTo(OperationEnum.DELETE) == 0) {
            command.execute();
            this.close();
        } else if (appTokenBinder.writeBeanIfValid(appToken)) {
            command.execute();
            this.close();
        }
    }

    private void prepareButtons(OperationEnum operation) {
        switch (operation) {
            case CREATE:
                submit.addClickListener(e -> executeCommand(new CreateTokenCommand(appTokenService, appToken)));
                break;
            case UPDATE:
                submit.addClickListener(e -> executeCommand(new UpdateTokenCommand(appTokenService, appToken)));
                break;
            case DELETE:
                submit.addClickListener(e -> executeCommand(new DeleteTokenCommand(appTokenService, appToken)));
                break;
        }
        cancel.addClickListener(e -> {
            this.canceled = true;
            this.close();
        });
    }

    public boolean isCanceled() {
        return canceled;
    }

    private void stylizeForm() {
        appName.setPlaceholder("ВВЕДИТЕ НАЗВАНИЕ");
        appName.setRequiredIndicatorVisible(true);
        appName.setWidthFull();
        token.setWidthFull();
        token.setReadOnly(true);

        buttons.setWidthFull();
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        content.setHeightFull();
        setWidth("400px");
        setHeightFull();
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
    }
}
