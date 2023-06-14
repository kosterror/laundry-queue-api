package ru.tsu.hits.kosterror.laundryqueueapi.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class JwtAuthentication extends AbstractAuthenticationToken {

    public JwtAuthentication(PersonData personData) {
        super(List.of(new SimpleGrantedAuthority(personData.getRole().toString())));
        setDetails(personData);
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return super.getDetails();
    }

}
