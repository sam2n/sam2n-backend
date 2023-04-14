package com.sam2n.backend.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthorityType {
    USER("USER"),
    ADMIN("ADMIN");
    private final String name;
}