package com.nottaras.prototype.converter;

import com.nottaras.prototype.config.prop.JwtProperties;
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
    private static final String PREFERRED_USERNAME_CLAIM = "preferred_username";
    private static final String RESOURCE_ACCESS_CLAIM = "resource_access";
    private static final String ROLE_PREFIX = "ROLE_";

    private final JwtProperties jwtProperties;

    @Override
    public final AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        var authorities = extractAuthorities(jwt);
        var principalClaim = jwt.getClaimAsString(PREFERRED_USERNAME_CLAIM);

        return new JwtAuthenticationToken(jwt, authorities, principalClaim);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        var roles = extractRolesFromResourceAccess(jwt);

        return roles.stream()
                .map(role -> ROLE_PREFIX + role)
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }

    @SuppressWarnings("unchecked")
    private List<String> extractRolesFromResourceAccess(Jwt jwt) {
        var resourceAccess = Optional.ofNullable(jwt.getClaim(RESOURCE_ACCESS_CLAIM))
                .map(Map.class::cast)
                .orElse(Map.of());
        var clientAccess = (Map<String, Object>) resourceAccess.getOrDefault(jwtProperties.clientId(), Map.of());

        return (List<String>) clientAccess.getOrDefault(ROLES_CLAIM, List.of());
    }
}
