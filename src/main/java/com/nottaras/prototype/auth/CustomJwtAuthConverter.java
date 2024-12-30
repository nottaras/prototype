package com.nottaras.prototype.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class CustomJwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String ROLES_CLAIM = "roles";
    private static final String SUB_CLAIM = "sub";
    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public final AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        var authorities = extractAuthorities(jwt);
        var principalClaim = jwt.getClaimAsString(SUB_CLAIM);

        return new JwtAuthenticationToken(jwt, authorities, principalClaim);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        var roles = extractRolesFromRealmAccess(jwt);

        return roles.stream()
                .map(role -> ROLE_PREFIX + role)
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }

    @SuppressWarnings("unchecked")
    private List<String> extractRolesFromRealmAccess(Jwt jwt) {
        var realmAccess = Optional.ofNullable(jwt.getClaim(REALM_ACCESS_CLAIM))
                .map(Map.class::cast)
                .orElse(Map.of());

        return (List<String>) realmAccess.getOrDefault(ROLES_CLAIM, List.of());
    }
}
