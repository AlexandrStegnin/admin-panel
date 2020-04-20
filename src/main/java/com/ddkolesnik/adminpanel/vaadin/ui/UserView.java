package com.ddkolesnik.adminpanel.vaadin.ui;

import com.ddkolesnik.adminpanel.configuration.support.OperationEnum;
import com.ddkolesnik.adminpanel.model.Role;
import com.ddkolesnik.adminpanel.model.User;
import com.ddkolesnik.adminpanel.service.RoleService;
import com.ddkolesnik.adminpanel.service.UserService;
import com.ddkolesnik.adminpanel.vaadin.custom.CustomAppLayout;
import com.ddkolesnik.adminpanel.vaadin.form.UserForm;
import com.ddkolesnik.adminpanel.vaadin.support.VaadinViewUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.stream.Collectors;

import static com.ddkolesnik.adminpanel.configuration.support.Location.USERS_PAGE;

/**
 * @author Alexandr Stegnin
 */

@Route(value = USERS_PAGE, layout = MainLayout.class)
public class UserView extends CustomAppLayout {

    private final UserService userService;

    private final RoleService roleService;

    private final Grid<User> grid; // сетка (таблица), основной элемент, в котором будут отображаться данные

    private final Button addNewBtn; // кнопка добавить нового пользователя

    private final ListDataProvider<User> dataProvider; // провайдер для Grid, он управляет данными

    private UserForm userForm;

    public UserView(UserService userService, RoleService roleService) {
        super(userService);
        this.userService = userService;
        this.roleService = roleService;
        this.grid = new Grid<>(); // инициализация Grid'a
        this.dataProvider = new ListDataProvider<>(getAllUsers()); // инициализация провайдера с вставкой в него данных
        this.addNewBtn = VaadinViewUtils.createButton(
                "СОЗДАТЬ ПОЛЬЗОВАТЕЛЯ", "add", "submit", "8px 13px 28px 7px");
        init(); // инициализируем форму
    }

    private void init() {
        addNewBtn.addClickListener(e -> showUserForm(new User(), OperationEnum.CREATE));
        grid.setDataProvider(dataProvider); // говорим grid'у, что за его данные отвечает провайдер
        /* Создаём колонки */
        grid.addColumn(User::getLogin)
                .setHeader("ЛОГИН")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(user -> user.getProfile().getFirstName())
                .setHeader("ИМЯ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(user -> user.getProfile().getLastName())
                .setHeader("ФАМИЛИЯ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(user -> user.getProfile().getPatronymic())
                .setHeader("ОТЧЕСТВО")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(user -> user.getProfile().getEmail())
                .setHeader("EMAIL")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(user -> user.getRoles().stream().map(Role::getTitle)
                .collect(Collectors.joining(", ")))
                .setHeader("РОЛИ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        // добавляем "составную" колонку (2 кнопки с обработчиками событий)
        grid.addComponentColumn(user ->
                VaadinViewUtils.makeEditorColumnActions(
                        e -> showUserForm(user, OperationEnum.UPDATE),
                        e -> showUserForm(user, OperationEnum.DELETE)))
                .setTextAlign(ColumnTextAlign.CENTER)
                .setEditorComponent(new Div())
                .setHeader("ДЕЙСТВИЯ")
                .setFlexGrow(2);

        /* вертикальный слой, на котором размещаем кнопку и под ней Grid */
        VerticalLayout verticalLayout = new VerticalLayout(addNewBtn, grid);
        verticalLayout.setAlignItems(FlexComponent.Alignment.END);

        // устанавливаем нашей странице контент в виде вертикального слоя, созданного выше
        setContent(verticalLayout);
    }

    private List<User> getAllUsers() {
        return userService.findAll();
    }

    // диалоговое окно с основными операциями
    private void showUserForm(final User user, final OperationEnum operation) {
        UserForm userForm = new UserForm(userService, roleService, user, operation);
        this.userForm = userForm;
        userForm.addOpenedChangeListener(event -> reload(!event.isOpened(), !this.userForm.isCanceled()));
        userForm.open();
    }

    private void reload(final boolean isClosed, final boolean isNotCanceled) {
        if (isClosed && isNotCanceled) dataProvider.refreshAll();
    }
}
