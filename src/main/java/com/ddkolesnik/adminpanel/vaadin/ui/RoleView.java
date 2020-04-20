package com.ddkolesnik.adminpanel.vaadin.ui;

import com.ddkolesnik.adminpanel.configuration.support.OperationEnum;
import com.ddkolesnik.adminpanel.model.Role;
import com.ddkolesnik.adminpanel.service.RoleService;
import com.ddkolesnik.adminpanel.service.UserService;
import com.ddkolesnik.adminpanel.vaadin.custom.CustomAppLayout;
import com.ddkolesnik.adminpanel.vaadin.form.RoleForm;
import com.ddkolesnik.adminpanel.vaadin.support.VaadinViewUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

import static com.ddkolesnik.adminpanel.configuration.support.Location.ROLES_PAGE;

@PageTitle("РОЛИ")
@Route(value = ROLES_PAGE, layout = MainLayout.class)
public class RoleView extends CustomAppLayout {

    private final RoleService roleService;
    private final Grid<Role> grid;
    private final Button addNewBtn;
    private final ListDataProvider<Role> dataProvider;
    private RoleForm roleForm;

    public RoleView(RoleService roleService, UserService userService) {
        super(userService);
        this.roleService = roleService;
        this.grid = new Grid<>();
        this.dataProvider = new ListDataProvider<>(getAll());
        this.addNewBtn = VaadinViewUtils.createButton("СОЗДАТЬ РОЛЬ", "add", "submit", "8px 13px 8px 13px");
        init();
    }

    private void init() {
        addNewBtn.addClickListener(e -> showRoleForm(OperationEnum.CREATE, new Role()));
        grid.setDataProvider(dataProvider);
        grid.getStyle().set("font-size", "14px");

        grid.addColumn(Role::getTitle)
                .setHeader("НАЗВАНИЕ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addComponentColumn(role -> VaadinViewUtils.makeEditorColumnActions(
                e -> showRoleForm(OperationEnum.UPDATE, role),
                e -> showRoleForm(OperationEnum.DELETE, role)))
                .setTextAlign(ColumnTextAlign.CENTER)
                .setEditorComponent(new Div())
                .setFlexGrow(2)
                .setHeader("ДЕЙСТВИЯ");

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(addNewBtn, grid);
        verticalLayout.setAlignItems(FlexComponent.Alignment.END);
        setContent(verticalLayout);
    }

    private List<Role> getAll() {
        return roleService.findAll();
    }

    private void showRoleForm(final OperationEnum operation, final Role role) {
        RoleForm roleForm = new RoleForm(operation, role, roleService);
        this.roleForm = roleForm;
        roleForm.addOpenedChangeListener(e -> refreshDataProvider(e.isOpened(), operation, role));
        roleForm.open();
    }

    private void refreshDataProvider(final boolean isOpened, final OperationEnum operation, final Role role) {
        if (!isOpened && !roleForm.isCanceled()) {
            if (operation.compareTo(OperationEnum.CREATE) == 0) dataProvider.getItems().add(role);
            else if (operation.compareTo(OperationEnum.DELETE) == 0) dataProvider.getItems().remove(role);
            dataProvider.refreshAll();
        }
    }

}
