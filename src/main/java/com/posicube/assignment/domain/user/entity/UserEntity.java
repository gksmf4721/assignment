package com.posicube.assignment.domain.user.entity;

import com.posicube.assignment.domain.user.dto.request.UserSaveReqDto;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String account;
    private String password;
    private String name;
    private String plan;

    protected UserEntity() {}

    public UserEntity(UserSaveReqDto dto) {
        this.account = dto.getAccount();
        this.password = dto.getPassword();
        this.name = dto.getName();
        this.plan = dto.getPlan();
    }
}
