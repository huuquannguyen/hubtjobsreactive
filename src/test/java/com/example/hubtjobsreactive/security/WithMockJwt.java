package com.example.hubtjobsreactive.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = WithMockJwtSecurityContextFactory.class)
public @interface WithMockJwt {
    long value() default 1L;

    String[] roles() default {};

    String email() default "ex@example.org";

    String name() default "test_user";
}
