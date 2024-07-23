package com.lamdangfixbug.qmshoe.user.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public enum Role {
    ADMIN(List.of(
            Permission.ADMIN_CREATE,
            Permission.ADMIN_DELETE,
            Permission.ADMIN_UPDATE,
            Permission.ADMIN_READ,
            Permission.ADMIN_ALL,
            Permission.MANAGER_CREATE,
            Permission.MANAGER_DELETE,
            Permission.MANAGER_UPDATE,
            Permission.MANAGER_READ,
            Permission.MANAGER_ALL
    )),
    MANAGER(List.of(
            Permission.MANAGER_CREATE,
            Permission.MANAGER_DELETE,
            Permission.MANAGER_UPDATE,
            Permission.MANAGER_READ,
            Permission.MANAGER_ALL
    )),
    READ(
            List.of(
                    Permission.ADMIN_READ,
                    Permission.MANAGER_READ
            )
    );

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
