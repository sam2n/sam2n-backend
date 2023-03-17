package com.sam2n.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sam2n.backend.config.DataBaseConfig;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User extends AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @NotNull
    @Pattern(regexp = DataBaseConfig.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;
    @JsonIgnore
    @NotNull

//    @Size(min = 60, max = 60)
//    @Column(length = 60, nullable = false)
    private String password;
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
    @NotNull
    @Column(nullable = false)
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
    private Instant resetDate = null;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    @JsonIgnore
    @ToString.Exclude
    private Wallet wallet;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "rel_user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")}
    )
    private Set<Authority> authorities;
}
