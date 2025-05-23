package com.sensormanager.iot.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1, initialValue = 6)
    @Column(unique = true)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String userEmail;
    private Long userCreatedAt;
    private Long userExpireAt;

    @Column(name = "user_status", nullable = false)
    @Builder.Default
    private Boolean userStatus = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_company", nullable = false)
    private Company company;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private final Set<Role> roleName = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.userCreatedAt = Instant.now().getEpochSecond();
        this.userExpireAt = Instant.now().plus(365, ChronoUnit.DAYS).getEpochSecond();
    }

    public boolean hasRole(String role) {
        for (Role authority : this.getRoleName()) {
            if (authority.getRoleName().equals(role)) {
                return true;
            }
        }
        return false;
    }

}
