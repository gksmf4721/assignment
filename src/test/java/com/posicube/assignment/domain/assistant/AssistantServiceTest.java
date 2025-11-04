package com.posicube.assignment.domain.assistant;

import com.posicube.assignment.LlmClient;
import com.posicube.assignment.common.dto.CommonParamDto;
import com.posicube.assignment.domain.assistant.dto.request.AssistantAnswerReqDto;
import com.posicube.assignment.domain.assistant.dto.response.AssistantAnswerResDto;
import com.posicube.assignment.domain.assistant.service.AssistantService;
import com.posicube.assignment.domain.user.service.UserService;
import com.posicube.assignment.error.GlobalErrorCode;
import com.posicube.assignment.error.exception.ApiCommonException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AssistantServiceTest {

    private static final Logger log = LoggerFactory.getLogger(AssistantServiceTest.class);

    @Mock
    private UserService userService;

    @Mock
    private LlmClient llmClient;

    @InjectMocks
    private AssistantService assistantService;

    private AssistantAnswerReqDto reqDto;
    private CommonParamDto common;

    @BeforeEach
    void setUp() {
        log.info("✅ MockitoMocks 초기화 완료");
        reqDto = new AssistantAnswerReqDto();
        reqDto.setQ("테스트 질문입니다");
        reqDto.setModel("gpt-4");

        common = new CommonParamDto();
        common.setUserId(1L);
    }

    @Test
    @DisplayName("질의 성공 - 정상 토큰 차감 및 응답 생성")
    void getAnswer_success() {
        log.info("▶ 테스트 시작: getAnswer_success");

        // given
        when(userService.findRemainingTokenByUser(1L)).thenReturn(1000);
        when(llmClient.query(eq("테스트 질문입니다"), eq("gpt-4"))).thenReturn("응답 내용");
        doNothing().when(userService).saveUsage(eq(1L), eq("gpt-4"), anyInt());
        doNothing().when(userService).setRemainingTokenByUser(eq(1L), anyInt(), any());

        // when
        AssistantAnswerResDto res = assistantService.getAnswer(reqDto, common);

        // then
        assertThat(res).isNotNull();
        assertThat(res.getAnswer()).isEqualTo("응답 내용");

        verify(userService).findRemainingTokenByUser(1L);
        verify(userService).saveUsage(eq(1L), eq("gpt-4"), anyInt());
        verify(userService).setRemainingTokenByUser(eq(1L), anyInt(), any());

        log.info("✅ 질의 요청 및 저장 검증 완료");
    }

    @Test
    @DisplayName("질의 실패 - LLM 예외 발생 시 ApiCommonException 반환")
    void getAnswer_fail_throwException() {
        log.info("▶ 테스트 시작: getAnswer_fail_throwException");

        when(userService.findRemainingTokenByUser(1L)).thenReturn(1000);
        when(llmClient.query(anyString(), anyString())).thenThrow(new RuntimeException("LLM 에러"));

        assertThatThrownBy(() -> assistantService.getAnswer(reqDto, common))
                .isInstanceOf(ApiCommonException.class)
                .extracting(ex -> ((ApiCommonException) ex).getCode())
                .isEqualTo(GlobalErrorCode.CALL_ABORT);

        log.warn("⚠️ LLM 호출 실패 시 예외 정상 발생");
    }

    @Test
    @DisplayName("질의 요청 시 토큰 계산 정확성 확인")
    void calculateTokens_correctness() throws Exception {
        log.info("▶ 테스트 시작: calculateTokens_correctness");

        // given
        String text = "1234567890";
        int expected = (int) Math.round(text.length() * 0.75);

        // when
        java.lang.reflect.Method method = AssistantService.class.getDeclaredMethod("calculateTokens", String.class);
        method.setAccessible(true);
        int result = (int) method.invoke(null, text);

        // then
        assertThat(result).isEqualTo(expected);
        log.debug("✅ 토큰 계산 결과 확인: {}", result);
    }
}