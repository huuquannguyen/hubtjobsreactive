package com.example.hubtjobsreactive.config.keycloak;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GrantedAuthoritiesExtractor implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final Converter<Jwt, Collection<GrantedAuthority>> defaultAuthorityConverter = new JwtGrantedAuthoritiesConverter();
    private static final String REALM_ACCESS = "realm_access";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Optional.ofNullable(jwt.getClaimAsMap(REALM_ACCESS))
                .map(realmAccess -> (List<String>) realmAccess.get("roles"))
                .orElse(Collections.emptyList())
                .stream()
                .map(a -> new SimpleGrantedAuthority("ROLE_" + a))
                .collect(Collectors.toList());
        authorities.addAll(Optional.ofNullable(defaultAuthorityConverter.convert(jwt)).orElse(Collections.emptyList()));
        return authorities;
    }
}
