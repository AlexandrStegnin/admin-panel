package com.ddkolesnik.adminpanel.vaadin.ui;

import com.ddkolesnik.adminpanel.configuration.support.OperationEnum;
import com.ddkolesnik.adminpanel.model.AppToken;
import com.ddkolesnik.adminpanel.service.AppTokenService;
import com.ddkolesnik.adminpanel.service.UserService;
import com.ddkolesnik.adminpanel.vaadin.custom.CustomAppLayout;
import com.ddkolesnik.adminpanel.vaadin.form.AppTokenForm;
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

import static com.ddkolesnik.adminpanel.configuration.support.Location.APP_TOKEN_PAGE;

/**
 * @author Alexandr Stegnin
 */

@PageTitle("ТОКЕНЫ")
@Route(value = APP_TOKEN_PAGE, layout = MainLayout.class)
public class AppTokenView extends CustomAppLayout {

    private final AppTokenService appTokenService;
    private final Grid<AppToken> grid;
    private final Button addNewBtn;
    private final ListDataProvider<AppToken> dataProvider;
    private AppTokenForm appTokenForm;

    public AppTokenView(AppTokenService appTokenService, UserService userService) {
        super(userService);
        this.appTokenService = appTokenService;
        this.grid = new Grid<>();
        this.dataProvider = new ListDataProvider<>(getAll());
        this.addNewBtn = VaadinViewUtils.createButton("СОЗДАТЬ ТОКЕН", "add", "submit", "8px 13px 8px 13px");
        init();
    }

    private void init() {
        addNewBtn.addClickListener(e -> showAppTokenForm(OperationEnum.CREATE, new AppToken()));
        grid.setDataProvider(dataProvider);

        grid.addColumn(AppToken::getAppName)
                .setHeader("НАЗВАНИЕ ПРИЛОЖЕНИЯ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addColumn(AppToken::getToken)
                .setHeader("ОПИСАНИЕ")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(1);

        grid.addComponentColumn(role -> VaadinViewUtils.makeEditorColumnActions(
                e -> showAppTokenForm(OperationEnum.UPDATE, role),
                e -> showAppTokenForm(OperationEnum.DELETE, role)))
                .setTextAlign(ColumnTextAlign.CENTER)
                .setEditorComponent(new Div())
                .setFlexGrow(2)
                .setHeader("ДЕЙСТВИЯ");
        grid.setClassName("my-grid");
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(addNewBtn, grid);
        verticalLayout.setAlignItems(FlexComponent.Alignment.END);
        setContent(verticalLayout);
    }

    private List<AppToken> getAll() {
        return appTokenService.findAll();
    }

    private void showAppTokenForm(final OperationEnum operation, final AppToken token) {
        AppTokenForm appTokenForm = new AppTokenForm(operation, token, appTokenService);
        this.appTokenForm = appTokenForm;
        appTokenForm.addOpenedChangeListener(e -> refreshDataProvider(e.isOpened(), operation, token));
        appTokenForm.open();
    }

    private void refreshDataProvider(final boolean isOpened, final OperationEnum operation, final AppToken token) {
        if (!isOpened && !appTokenForm.isCanceled()) {
            if (operation.compareTo(OperationEnum.CREATE) == 0) dataProvider.getItems().add(token);
            else if (operation.compareTo(OperationEnum.DELETE) == 0) dataProvider.getItems().remove(token);
            dataProvider.refreshAll();
        }
    }

}
