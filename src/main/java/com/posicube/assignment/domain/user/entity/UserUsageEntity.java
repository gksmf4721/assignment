package com.posicube.assignment.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_usage")
public class UserUsageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 사용자 FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // 모델명 (gpt-4o-mini, gpt-5)
    @Column(nullable = false)
    private String modelName;

    // 사용한 토큰 수
    @Column(nullable = false)
    private int tokens;
}
