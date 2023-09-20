package com.example.hubtjobsreactive.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Map;

public class WithMockJwtSecurityContextFactory implements WithSecurityContextFactory<WithMockJwt> {
    @Override
    public SecurityContext createSecurityContext(WithMockJwt annotation) {
        var jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", annotation.name())
                .claim("user", Map.of("email", annotation.email()))
                .build();

        var authorities = AuthorityUtils.createAuthorityList(annotation.roles());
        var token = new JwtAuthenticationToken(jwt, authorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(token);
        return context;

    }
}