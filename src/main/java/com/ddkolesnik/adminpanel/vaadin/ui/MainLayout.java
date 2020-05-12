package com.ddkolesnik.adminpanel.vaadin.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

/**
 * @author Alexandr Stegnin
 */
@Theme(value = Material.class, variant = Material.LIGHT)
public class MainLayout extends Div implements RouterLayout, PageConfigurator {

    @Override
    public void configurePage(InitialPageSettings settings) {

        settings.addMetaTag("viewport", "width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no");
        // Favicon
        settings.addLink("shortcut icon", "./icons/favicon.ico");
        settings.addFavIcon("icon", "./icons/icon-192.png", "192x192");
        // Google Fonts
        settings.addLink("stylesheet", "https://fonts.googleapis.com/css?family=Roboto:400,700&subset=latin,cyrillic-ext");
        settings.addLink("stylesheet", "https://fonts.googleapis.com/icon?family=Material+Icons");
        // Bootstrap Core Css
        settings.addLink("stylesheet", "./plugins/bootstrap/css/bootstrap.css");
//        // Waves Effect Css
        settings.addLink("stylesheet", "./plugins/node-waves/waves.css");
//        // Animation Css
        settings.addLink("stylesheet", "./plugins/animate-css/animate.css");
//        // Custom Css
        settings.addLink("stylesheet", "./css/style.css");

//        // Materialize Css
        settings.addLink("stylesheet", "./css/materialize.css");

        settings.addLink("stylesheet", "./css/grid-style.css");

//        // jQuery & bootstrap js
        settings.getUi().getPage().addJavaScript("../plugins/jquery/jquery.js");
        settings.getUi().getPage().addJavaScript("../plugins/bootstrap/js/bootstrap.js");

    }

}
