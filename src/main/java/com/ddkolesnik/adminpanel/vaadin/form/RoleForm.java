package com.ddkolesnik.adminpanel.vaadin.form;

import com.ddkolesnik.adminpanel.command.Command;
import com.ddkolesnik.adminpanel.command.role.CreateRoleCommand;
import com.ddkolesnik.adminpanel.command.role.DeleteRoleCommand;
import com.ddkolesnik.adminpanel.command.role.UpdateRoleCommand;
import com.ddkolesnik.adminpanel.configuration.support.OperationEnum;
import com.ddkolesnik.adminpanel.model.Role;
import com.ddkolesnik.adminpanel.service.RoleService;
import com.ddkolesnik.adminpanel.vaadin.support.VaadinViewUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;


/**
 * @author Alexandr Stegnin
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleForm extends Dialog {

    final RoleService roleService;
    final Role role;
    final TextField title;
    final TextField humanized;
    final Binder<Role> roleBinder;
    final OperationEnum operation;
    final Button cancel;
    final HorizontalLayout buttons;
    final VerticalLayout content;
    final Button submit;
    private boolean canceled = false;

    public RoleForm(OperationEnum operation, Role role, RoleService roleService) {
        this.title = new TextField("НАЗВАНИЕ");
        this.humanized = new TextField("ОТОБРАЖАЕМОЕ ИМЯ");
        this.roleBinder = new BeanValidationBinder<>(Role.class);
        this.roleService = roleService;
        this.operation = operation;
        this.submit = VaadinViewUtils.createButton(
                operation.name.toUpperCase(), "", "submit", "8px 10px 8px 10px");
        this.cancel = VaadinViewUtils.createButton("ОТМЕНИТЬ", "", "cancel", "8px 10px 8px 10px");
        this.buttons = new HorizontalLayout();
        this.content = new VerticalLayout();
        this.role = role;
        init();
    }

    private void init() {
        prepareButtons(operation);
        stylizeForm();
        buttons.add(submit, cancel);
        content.add(title, humanized, buttons);
        add(content);
        roleBinder.setBean(role);
        roleBinder.bindInstanceFields(this);
    }

    private void executeCommand(Command command) {
        if (operation.compareTo(OperationEnum.DELETE) == 0) {
            command.execute();
            this.close();
        } else if (roleBinder.writeBeanIfValid(role)) {
            command.execute();
            this.close();
        }
    }

    private void prepareButtons(OperationEnum operation) {
        switch (operation) {
            case CREATE:
                submit.addClickListener(e -> executeCommand(new CreateRoleCommand(role, roleService)));
                break;
            case UPDATE:
                submit.addClickListener(e -> executeCommand(new UpdateRoleCommand(role, roleService)));
                break;
            case DELETE:
                submit.addClickListener(e -> executeCommand(new DeleteRoleCommand(role, roleService)));
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
        title.setPlaceholder("ВВЕДИТЕ НАЗВАНИЕ");
        title.setRequiredIndicatorVisible(true);
        title.setWidthFull();

        humanized.setPlaceholder("ВВЕДИТЕ ОТОБРАЖАЕМОЕ ИМЯ");
        humanized.setRequiredIndicatorVisible(true);
        humanized.setWidthFull();

        buttons.setWidthFull();
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        content.setHeightFull();
        setHeightFull();
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
    }
}
