package com.ddkolesnik.adminpanel.configuration.support;

import java.util.Locale;

/**
 * Файл для хранения путей приложения
 *
 * @author Alexandr Stegnin
 */

public class Location {

    public static final String AUTHORITIES_KEY = "authorities";

    public static final String PATH_SEPARATOR = "/";

    /* Application pages constants */
    public static final String LOGIN_PAGE = "login";
    public static final String LOGOUT_PAGE = "logout";

    public static final String APP_TOKEN_PAGE = "tokens";

    public static final String ADMIN_PAGE = "admin";

    public static final String USERS_PAGE = "users";

    public static final String ROLES_PAGE = "roles";

    public static final Locale LOCALE_RU = new Locale("ru", "RU");

    public static final String LOGOUT_URL = PATH_SEPARATOR + LOGOUT_PAGE;
    public static final String LOGIN_URL = PATH_SEPARATOR + LOGIN_PAGE;

    public static final String[] ALL_WEB_IGNORING_MATCHERS = {
            // Vaadin Flow static resources
            "/VAADIN/**",

            // the standard favicon URI
            "/favicon.ico",

            // web application manifest
            "/manifest.json",

            // icons and images
            "/icons/**",
            "/images/**",

            // (development mode) static resources
            "/frontend/**",

            // (development mode) webjars
            "/webjars/**",

            // (development mode) H2 debugging console
            "/h2-console/**",

            // (production mode) static resources
            "/frontend-es5/**", "/frontend-es6/**"
    };

    public static final String[] ALL_HTTP_MATCHERS = {
            "/VAADIN/**", "/HEARTBEAT/**", "/UIDL/**", "/resources/**",
            "/login", "/login**", "/login/**", "/manifest.json", "/icons/**", "/images/**",
            // (development mode) static resources
            "/frontend/**",
            // (development mode) webjars
            "/webjars/**",
            // (development mode) H2 debugging console
            "/h2-console/**",
            // (production mode) static resources
            "/frontend-es5/**", "/frontend-es6/**"
    };
}
