package com.posicube.assignment.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSaveReqDto {

    private String account;
    private String password;
    private String name;
    private String plan;
}
