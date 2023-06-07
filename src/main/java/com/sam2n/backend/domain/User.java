package com.sam2n.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sam2n.backend.domain.enumeration.Provider;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
@Table(name = "sam2n_users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User extends AbstractAuditingEntity implements Serializable, UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(nullable = false, updatable = false)
    private UUID id;
    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;
    @JsonIgnore
    @NotNull
//    @Size(min = 60, max = 60)
//    @Column(length = 60, nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Provider provider;
    @Size(max = 50)
    @Column(length = 50)
    private String firstName;
    @Size(max = 50)
    @Column(length = 50)
    private String lastName;
    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;
    @Column(nullable = false)
    @Builder.Default
    private boolean activated = false;
    @Size(max = 256)
    @Column(length = 256)
    private String imageUrl;
    @Size(max = 20)
    @Column(length = 20)
    @JsonIgnore
    private String activationKey;
    @Size(max = 20)
    @Column(length = 20)
    @JsonIgnore
    private String resetKey;
    @Builder.Default
    private Instant resetDate = null;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    @JsonIgnore
    @ToString.Exclude
    private Wallet wallet;
    @ManyToOne
    @JsonIgnoreProperties(value = {"company_id"}, allowSetters = true)
    @ToString.Exclude
    private Company company;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "rel_user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")}
    )
    private Set<Authority> authorities;

    @Override
    public List<GrantedAuthority> getAuthorities() {

        return authorities.stream()
                .map(authority -> (GrantedAuthority) authority::getName)
                .toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

