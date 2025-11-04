package com.posicube.assignment.domain.user.repository;

import com.posicube.assignment.domain.user.entity.UserUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserUsageRepository extends JpaRepository<UserUsageEntity, Long> {
    List<UserUsageEntity> findByUserId(Long userId);
}