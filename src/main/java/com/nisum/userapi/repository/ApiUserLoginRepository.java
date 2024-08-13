package com.nisum.userapi.repository;

import com.nisum.userapi.entity.ApiUserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApiUserLoginRepository extends JpaRepository<ApiUserLogin, UUID> {
    @Query(value = "SELECT MAX(login_ts) FROM api_user_login WHERE user_id = ?1", nativeQuery = true)
    Optional<LocalDateTime> findLastLogin(UUID userId);
}
