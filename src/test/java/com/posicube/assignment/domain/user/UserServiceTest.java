package com.posicube.assignment.domain.user;

import com.posicube.assignment.domain.assistant.dto.response.AssistantModelResDto;
import com.posicube.assignment.domain.user.dto.request.UserSaveReqDto;
import com.posicube.assignment.common.dto.CommonParamDto;
import com.posicube.assignment.domain.user.dto.response.UserUsageResDto;
import com.posicube.assignment.domain.user.entity.UserEntity;
import com.posicube.assignment.domain.user.entity.UserUsageEntity;
import com.posicube.assignment.domain.user.repository.UserRepository;
import com.posicube.assignment.domain.user.repository.UserUsageRepository;
import com.posicube.assignment.domain.user.service.UserService;
import com.posicube.assignment.error.exception.ApiCommonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserUsageRepository userUsageRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        log.info("✅ MockitoMocks 초기화 완료");
    }

    @Test
    @DisplayName("유저 저장 성공")
    void saveUser_success() {
        log.info("▶ 테스트 시작: saveUser_success");

        UserSaveReqDto dto = new UserSaveReqDto();
        dto.setAccount("test");
        dto.setPassword("1234");
        dto.setName("홍길동");
        dto.setPlan("PRO");

        log.debug("  - DTO 생성 완료: {}", dto.getAccount());

        userService.saveUser(dto);

        log.debug("  - userService.saveUser 호출 완료");

        verify(userRepository, times(1)).save(any(UserEntity.class));
        log.info("✅ userRepository.save 호출 검증 완료");
    }

    @Test
    @DisplayName("모델 사용 내역 저장 성공")
    void saveUsage_success() {
        log.info("▶ 테스트 시작: saveUsage_success");

        UserEntity user = mock(UserEntity.class);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.saveUsage(1L, "gpt-5", 1000);

        verify(userUsageRepository, times(1)).save(any(UserUsageEntity.class));
        log.info("✅ userUsageRepository.save 호출 검증 완료");
    }

    @Test
    @DisplayName("유저를 찾을 수 없을 때 예외 발생")
    void saveUsage_notFoundUser() {
        log.info("▶ 테스트 시작: saveUsage_notFoundUser");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.saveUsage(1L, "gpt-5", 1000))
                .isInstanceOf(ApiCommonException.class);

        log.warn("⚠️ 예상대로 ApiCommonException 발생");
    }

    @Test
    @DisplayName("사용 내역 조회 성공 - PRO 요금제")
    void findUserUsage_success() {
        log.info("▶ 테스트 시작: findUserUsage_success");

        UserEntity user = mock(UserEntity.class);
        when(user.getPlan()).thenReturn("PRO");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserUsageEntity usage1 = UserUsageEntity.builder()
                .user(user)
                .modelName("gpt-5")
                .tokens(1000)
                .build();

        UserUsageEntity usage2 = UserUsageEntity.builder()
                .user(user)
                .modelName("gpt-4o-mini")
                .tokens(2000)
                .build();

        when(userUsageRepository.findByUserId(1L)).thenReturn(List.of(usage1, usage2));

        CommonParamDto common = new CommonParamDto(null, 1L);
        UserUsageResDto result = userService.findUserUsage(common);

        log.debug("  - result: plan={}, used={}, remain={}",
                result.getPlan(), result.getUsedTokens(), result.getRemainingTokens());

        assertThat(result.getPlan()).isEqualTo("PRO");
        assertThat(result.getQuota()).isEqualTo(50000);
        assertThat(result.getUsedTokens()).isEqualTo(3000);
        assertThat(result.getRemainingTokens()).isEqualTo(47000);
        assertThat(result.getModels()).hasSize(2);

        log.info("✅ 모델 사용 내역 조회 테스트 통과");
    }

    @Test
    @DisplayName("유저 정보 조회 실패 시 예외 발생")
    void findUserById_notFound() {
        log.info("▶ 테스트 시작: findUserById_notFound");

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findUserById(99L))
                .isInstanceOf(ApiCommonException.class);

        log.warn("⚠️ findUserById 예외 정상 발생");
    }
}
