package com.posicube.assignment.domain.user.service;

import com.posicube.assignment.common.dto.CommonParamDto;
import com.posicube.assignment.common.enums.RedisKeys;
import com.posicube.assignment.domain.assistant.dto.response.AssistantModelResDto;
import com.posicube.assignment.domain.assistant.enums.AssistantModelType;
import com.posicube.assignment.domain.user.dto.request.UserSaveReqDto;
import com.posicube.assignment.domain.user.dto.response.UserUsageResDto;
import com.posicube.assignment.domain.user.entity.UserEntity;
import com.posicube.assignment.domain.user.entity.UserUsageEntity;
import com.posicube.assignment.domain.user.enums.UserPlanType;
import com.posicube.assignment.domain.user.repository.UserCacheRepository;
import com.posicube.assignment.domain.user.repository.UserRepository;
import com.posicube.assignment.domain.user.repository.UserUsageRepository;
import com.posicube.assignment.error.GlobalErrorCode;
import com.posicube.assignment.error.exception.ApiCommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserUsageRepository userUsageRepository;
    private final UserCacheRepository userCacheRepository;

    // 유저 정보 저장
    public void saveUser(UserSaveReqDto dto) {
        userRepository.save(new UserEntity(dto));
    }

    // 모델 사용 내역 저장
    public void saveUsage(Long userId, String modelName, int tokens) {
        // 유저 검증
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiCommonException(GlobalErrorCode.NOT_FOUND_USER));

        // 모델 검증
        if (!AssistantModelType.existsPriceByName(modelName)) {
            throw new ApiCommonException(GlobalErrorCode.NOT_FOUND_ASSISTANT_MODEL);
        }

        UserUsageEntity usage = UserUsageEntity.builder()
                .user(user)
                .modelName(modelName)
                .tokens(tokens)
                .build();

        userUsageRepository.save(usage);
    }

    // 모델 사용량 조회
    public UserUsageResDto findUserUsage(CommonParamDto common) {
        Long userId = common.getUserId();
        List<UserUsageEntity> usages = userUsageRepository.findByUserId(userId);
        Optional<UserEntity> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) throw new ApiCommonException(GlobalErrorCode.NOT_FOUND_USER);
        if (usages.isEmpty()) return new UserUsageResDto();

        // 사용자 요금제(plan)
        String plan = userOpt.get().getPlan();

        // 요금제별 총 토큰(quota) 매핑
        int quota = UserPlanType.getQuotaByName(plan);
        int totalTokens = 0;
        double totalPrice = 0;

        Map<String, AssistantModelResDto> modelUsageMap = new HashMap<>();

        for (UserUsageEntity usage : usages) {
            String modelName = usage.getModelName();
            int tokens = usage.getTokens();

            double pricePerK = AssistantModelType.getPriceByName(modelName);
            double price = Math.round((pricePerK / 1000 * usage.getTokens()) * 1000.0) / 1000.0;

            totalTokens += tokens;
            totalPrice += price;

            modelUsageMap.merge(
                    modelName,
                    new AssistantModelResDto(modelName, tokens, price),
                    (oldVal, newVal) -> new AssistantModelResDto(
                            oldVal.getName(),
                            oldVal.getTokens() + newVal.getTokens(),
                            oldVal.getPrice() + newVal.getPrice()
                    )
            );
        }

        int remainingTokens = quota - totalTokens;

        return UserUsageResDto.of(plan, quota, totalTokens, remainingTokens, totalPrice, modelUsageMap);
    }


    // ======== Service to Service ==========

    // 유저 정보 조회
    public UserEntity findUserById(Long userId) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new ApiCommonException(GlobalErrorCode.NOT_FOUND_USER);
        }

        return userOpt.get();
    }

    // Redis 사용 가능한 토큰량 조회
    public int findRemainingTokenByUser(Long userId) {
        String key = RedisKeys.USER_TOKEN.getKey(userId);
        String tokenStr = userCacheRepository.findRemainingTokenByUser(key);
        String plan = findUserById(userId).getPlan();

        int remainingTokens;

        if (tokenStr == null) {
            // 키가 없으면 plan별 초기값
            remainingTokens = UserPlanType.getQuotaByName(plan);
        } else {
            remainingTokens = Integer.parseInt(tokenStr);

            // 사용 가능한 토큰이 없는 경우
            if (remainingTokens <= 0) {
                throw new ApiCommonException(GlobalErrorCode.NOT_HAVE_TOKEN);
            }
        }

        return remainingTokens;
    }

    // Redis 사용 가능한 토큰량 저장
    public void setRemainingTokenByUser(Long userId, int value, LocalDateTime now) {
        String key = RedisKeys.USER_TOKEN.getKey(userId);

        LocalDateTime midnight = LocalDateTime.of(now.toLocalDate(), LocalTime.MAX);
        long secondsUntilMidnight = java.time.Duration.between(now, midnight).getSeconds();
        Duration ttl = Duration.ofSeconds(secondsUntilMidnight);

        userCacheRepository.setRemainingTokenByUser(key, String.valueOf(value), ttl);
    }
}
