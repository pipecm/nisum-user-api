package com.nisum.userapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "api_user_login")
public class ApiUserLogin {

    @Id
    @Column(name = "login_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "login_token")
    private String token;

    @Column(name = "login_ts")
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private ApiUser apiUser;

    public ApiUserLogin(ApiUser apiUser, String token) {
        this.apiUser = apiUser;
        this.token = token;
        this.timestamp = LocalDateTime.now();
    }
}
