package com.nisum.userapi.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "api_user")
public class ApiUser {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_password")
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    @Column(name = "user_active")
    private boolean active;

    private LocalDateTime lastLogin;

    private String lastToken;

    @OneToMany(mappedBy = "apiUser", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ApiUserPhone> phones;

    @OneToMany(mappedBy = "apiUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ApiUserLogin> loginHistory;
}
