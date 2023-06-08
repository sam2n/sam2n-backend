package com.sam2n.backend.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthorityType {
    USER("user", "users"),
    ADMIN("admin", "admins");
    private final String dbName;
    private final String claimName;
}