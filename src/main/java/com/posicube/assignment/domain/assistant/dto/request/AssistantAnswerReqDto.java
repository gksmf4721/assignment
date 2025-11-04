package com.posicube.assignment.domain.assistant.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistantAnswerReqDto {

    private String q;
    private String model;
}
