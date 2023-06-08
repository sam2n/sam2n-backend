package com.sam2n.backend.service;

import com.sam2n.backend.domain.Authority;
import com.sam2n.backend.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    public List<Authority> initAndSave(String... inputAuthorities) {
        List<Authority> authorities = authorityRepository.findAll();
        if (authorities.isEmpty()) {
            authorities = Arrays.stream(inputAuthorities)
                    .map(Authority::new)
                    .toList();
            authorities = authorityRepository.saveAll(authorities);
        }
        return authorities;
    }
}
