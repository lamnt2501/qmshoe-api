package com.lamdangfixbug.qmshoe.user.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public enum Role {
    ADMIN(List.of(
            Permission.ALL
    )),
    ACCOUNTANT(List.of(
            Permission.READ,
            Permission.PAYMENT_UPDATE
    )),
    ORDER_PROCESSOR(List.of(
            Permission.READ,
            Permission.ORDER_UPDATE
    ));

    private final List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(permissions.stream().map(p -> new SimpleGrantedAuthority(p.name())).toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));
        return authorities;
    }
}
