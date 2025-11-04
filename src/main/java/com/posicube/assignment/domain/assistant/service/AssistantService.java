package com.posicube.assignment.domain.assistant.service;

import com.posicube.assignment.LlmClient;
import com.posicube.assignment.common.dto.CommonParamDto;
import com.posicube.assignment.domain.assistant.dto.request.AssistantAnswerReqDto;
import com.posicube.assignment.domain.assistant.dto.response.AssistantAnswerResDto;
import com.posicube.assignment.domain.user.service.UserService;
import com.posicube.assignment.error.GlobalErrorCode;
import com.posicube.assignment.error.exception.ApiCommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AssistantService {

    private final UserService userService;
    private final LlmClient llmClient;

    // 질의
    public AssistantAnswerResDto getAnswer(AssistantAnswerReqDto dto, CommonParamDto common) throws ApiCommonException {
        try {
            LocalDateTime now = LocalDateTime.now();
            Long userId = common.getUserId();
            int tokenCost = calculateTokens(dto.getQ());

            // 마지막 요청까지 허용: 음수도 저장 가능
            int remainingTokens = userService.findRemainingTokenByUser(userId);
            int updatedTokens = remainingTokens - tokenCost;

            // LLM 호출 및 요청 내역 DB 저장
            String result = llmClient.query(dto.getQ(), dto.getModel());
            userService.saveUsage(userId, dto.getModel(), tokenCost);

            // Redis 값 저장
            userService.setRemainingTokenByUser(userId, updatedTokens, now);

            return AssistantAnswerResDto.of(result);

        } catch (RuntimeException e) {
            throw new ApiCommonException(GlobalErrorCode.CALL_ABORT);
        }
    }

    // 토큰 계산
    private static int calculateTokens(String q) {
        if (q == null) return 0;
        double tokens = q.length() * 0.75;
        return (int) Math.round(tokens);
    }
}
