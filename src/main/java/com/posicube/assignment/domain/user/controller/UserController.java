package com.posicube.assignment.domain.user.controller;

import com.posicube.assignment.common.dto.CommonParamDto;
import com.posicube.assignment.common.dto.CommonResDto;
import com.posicube.assignment.common.dto.ResponseDto;
import com.posicube.assignment.domain.user.dto.request.UserSaveReqDto;
import com.posicube.assignment.domain.user.dto.response.UserUsageResDto;
import com.posicube.assignment.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**`
     * 사용자 추가
     * */
    @PostMapping("/users")
    public ResponseDto<CommonResDto> saveUser(@RequestBody UserSaveReqDto userSaveReqDto) {
        userService.saveUser(userSaveReqDto);
        return ResponseDto.of(CommonResDto.success());
    }

    /**`
     * 사용량 조회
     * */
    @GetMapping("/usage")
    public ResponseDto<UserUsageResDto> findUsage(CommonParamDto common) {
        UserUsageResDto result = userService.findUserUsage(common);
        return ResponseDto.of(result);
    }
}
