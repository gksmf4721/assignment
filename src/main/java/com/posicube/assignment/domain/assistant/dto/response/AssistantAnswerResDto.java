package com.posicube.assignment.domain.assistant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssistantAnswerResDto {

    private String answer;

    public static AssistantAnswerResDto of(
            String answer
    ) {
        return AssistantAnswerResDto.builder()
                .answer(answer)
                .build();
    }
}
