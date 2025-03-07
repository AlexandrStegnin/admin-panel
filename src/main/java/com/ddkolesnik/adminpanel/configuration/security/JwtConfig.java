package com.ddkolesnik.adminpanel.configuration.security;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Alexandr Stegnin
 */
// To use this class outside. You have to
// 1. Define it as a bean, either by adding @Component or use @Bean to instantiate an object from it
// 2. Use the @Autowire to ask spring to auto create it for you, and inject all the values.

// So, If you tried to create an instance manually (i.e. new JwtConfig()). This won't inject all the values.
// Because you didn't ask Spring to do so (it's done by you manually!).
// Also, if, at any time, you tried to instantiate an object that's not defined as a bean
// Don't expect Spring will autowire the fields inside that class object.

@Getter        // lombok will create getters auto.
@ToString        // [IMP] You need to install lombok jar file: https://stackoverflow.com/a/11807022
public class JwtConfig {

    // Spring doesn't inject/autowire to "static" fields.
    // Link: https://stackoverflow.com/a/6897406
    @Value("${security.jwt.uri:/auth/**}")
    private String Uri;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.expiration:#{1000*60*60*24}}") // 24 hours in milliseconds
    private int expiration;

    @Value("${security.jwt.secret:SecretKeyToGenJWTs}")
    private String secret;

}
