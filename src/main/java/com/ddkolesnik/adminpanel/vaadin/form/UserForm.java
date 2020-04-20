package com.ddkolesnik.adminpanel.vaadin.form;

import com.ddkolesnik.adminpanel.command.Command;
import com.ddkolesnik.adminpanel.command.user.CreateUserCommand;
import com.ddkolesnik.adminpanel.command.user.DeleteUserCommand;
import com.ddkolesnik.adminpanel.command.user.UpdateUserCommand;
import com.ddkolesnik.adminpanel.configuration.support.OperationEnum;
import com.ddkolesnik.adminpanel.model.Role;
import com.ddkolesnik.adminpanel.model.User;
import com.ddkolesnik.adminpanel.model.UserProfile;
import com.ddkolesnik.adminpanel.service.RoleService;
import com.ddkolesnik.adminpanel.service.UserService;
import com.ddkolesnik.adminpanel.vaadin.support.VaadinViewUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */
public class UserForm extends Dialog {

    private final UserService userService;
    private final RoleService roleService;
    private final User user;
    private final TextField login;
    private final TextField firstName;
    private final TextField lastName;
    private final TextField patronymic;
    private final TextField password;
    private final EmailField email;
    private final MultiselectComboBox<Role> roles;
    private final Checkbox accountNonLocked;

    private final Binder<User> userBinder;
    private final Binder<UserProfile> profileBinder;
    private final OperationEnum operation;
    private final Button cancel;
    private final HorizontalLayout buttons;
    private final VerticalLayout content;
    private Button submit;
    private boolean canceled = false;

    public UserForm(UserService userService, RoleService roleService, User user, OperationEnum operation) {
        this.userService = userService;
        this.roleService = roleService;
        this.login = new TextField("ИМЯ ПОЛЬЗОВАТЕЛЯ");
        this.firstName = new TextField("ИМЯ");
        this.lastName = new TextField("ФАМИЛИЯ");
        this.patronymic = new TextField("ОТЧЕСТВО");
        this.password = new TextField("ПАРОЛЬ");
        this.email = new EmailField("EMAIL");
        this.roles = new MultiselectComboBox<>();
        this.accountNonLocked = new Checkbox("АККАУНТ НЕ БЛОКИРОВАН");

        this.userBinder = new BeanValidationBinder<>(User.class);
        this.profileBinder = new BeanValidationBinder<>(UserProfile.class);
        this.operation = operation;
        this.submit = VaadinViewUtils.createButton(operation.name.toUpperCase(), "", "submit", "8px 10px 22px 8px");
        this.cancel = VaadinViewUtils.createButton("ОТМЕНИТЬ", "", "cancel", "8px 10px 22px 8px");
        this.buttons = new HorizontalLayout();
        this.content = new VerticalLayout();
        this.user = user;
        init();
    }

    private void init() {
        prepareButtons(operation);
        stylizeForm();
        roles.setItems(getAllRoles());
        buttons.add(submit, cancel);
        content.add(login, lastName, firstName, patronymic, email, roles, password, accountNonLocked, buttons);
        add(content);
        userBinder.setBean(user);
        userBinder.bindInstanceFields(this);
        profileBinder.setBean(user.getProfile());
        profileBinder.bindInstanceFields(this);
    }

    private void prepareButtons(OperationEnum operation) {
        switch (operation) {
            case CREATE:
                submit.addClickListener(e -> executeCommand(new CreateUserCommand(user, userService)));
                break;
            case UPDATE:
                submit.addClickListener(e -> executeCommand(new UpdateUserCommand(user, userService)));
                break;
            case DELETE:
                submit.addClickListener(e -> executeCommand(new DeleteUserCommand(user, userService)));
                break;
        }
        cancel.addClickListener(e -> {
            this.canceled = true;
            this.close();
        });
    }

    private void executeCommand(Command command) {
        if (command instanceof DeleteUserCommand) {
            command.execute();
            this.close();
        } else if (userBinder.writeBeanIfValid(user) &&
                    profileBinder.writeBeanIfValid(user.getProfile())) {
            command.execute();
            this.close();
        }
    }

    public boolean isCanceled() {
        return canceled;
    }

    private void stylizeForm() {
        accountNonLocked.setVisible(operation.compareTo(OperationEnum.UPDATE) == 0);

        login.setPlaceholder("ИМЯ ПОЛЬЗОВАТЕЛЯ");
        login.setRequiredIndicatorVisible(true);
        login.setWidthFull();

        firstName.setPlaceholder("ИМЯ");
        firstName.setRequired(true);
        firstName.setRequiredIndicatorVisible(true);
        firstName.setWidthFull();

        lastName.setPlaceholder("ФАМИЛИЯ");
        lastName.setRequired(true);
        lastName.setRequiredIndicatorVisible(true);
        lastName.setWidthFull();

        patronymic.setPlaceholder("ОТЧЕСТВО");
        patronymic.setWidthFull();

        email.setPlaceholder("EMAIL");
//        email.setPreventInvalidInput(true);
        email.setRequiredIndicatorVisible(true);
        email.setWidthFull();

        roles.setLabel("ВЫБЕРИТЕ РОЛИ");
        roles.setItemLabelGenerator(Role::getTitle);
        roles.setItems(getAllRoles());

        password.setPlaceholder("ПАРОЛЬ");
        password.setRequired(true);
        password.setRequiredIndicatorVisible(true);
        password.setWidthFull();

        buttons.setWidthFull();
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        content.setHeightFull();
        setWidth("400px");
        setHeightFull();
    }

    private List<Role> getAllRoles() {
        return roleService.findAll();
    }

}
