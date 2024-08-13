package com.nisum.userapi.repository;

import com.nisum.userapi.entity.ApiUserPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApiUserPhoneRepository extends JpaRepository<ApiUserPhone, UUID> {
}
