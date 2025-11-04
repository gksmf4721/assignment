package com.posicube.assignment.domain.assistant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssistantModelResDto {
    private String name;
    private int tokens;
    private double price;
}