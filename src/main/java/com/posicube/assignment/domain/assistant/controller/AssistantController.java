package com.posicube.assignment.domain.assistant.controller;

import com.posicube.assignment.common.dto.CommonParamDto;
import com.posicube.assignment.common.dto.ResponseDto;
import com.posicube.assignment.domain.assistant.dto.request.AssistantAnswerReqDto;
import com.posicube.assignment.domain.assistant.dto.response.AssistantAnswerResDto;
import com.posicube.assignment.domain.assistant.service.AssistantService;
import com.posicube.assignment.error.exception.ApiCommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AssistantController {

    private final AssistantService assistantService;

    /**`
     * 질의
     * */
    @PostMapping("/query")
    public ResponseDto<AssistantAnswerResDto> saveUser(
            @RequestBody AssistantAnswerReqDto assistantAnswerReqDto,
            CommonParamDto common
    ) throws ApiCommonException {
        AssistantAnswerResDto result = assistantService.getAnswer(assistantAnswerReqDto, common);
        return ResponseDto.of(result);
    }
}
