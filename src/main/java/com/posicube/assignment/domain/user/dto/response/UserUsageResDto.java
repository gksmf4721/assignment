package com.posicube.assignment.domain.user.dto.response;

import com.posicube.assignment.domain.assistant.dto.response.AssistantModelResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class UserUsageResDto {

    private String plan;
    private int quota;
    private int usedTokens;
    private int remainingTokens;
    private double totalPrice;
    private List<AssistantModelResDto> models;

    public UserUsageResDto() {}

    public static UserUsageResDto of(
            String plan,
            int quota,
            int usedTokens,
            int remainingTokens,
            double totalPrice,
            Map<String, AssistantModelResDto> modelMap
    ) {

        List<AssistantModelResDto> models = modelMap.values().stream().toList();

        return UserUsageResDto.builder()
                .plan(plan)
                .quota(quota)
                .usedTokens(usedTokens)
                .remainingTokens(remainingTokens)
                .totalPrice(totalPrice)
                .models(models)
                .build();
    }
}
