package com.posicube.assignment.domain.user.repository;

import com.posicube.assignment.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
