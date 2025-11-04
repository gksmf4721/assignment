package com.posicube.assignment;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class LlmClient {
    private final Random random = new Random();

    public String query(String query, String model) {
        try {
            // 10% 확률로 에러 발생
            if (random.nextInt(10) == 0) {
                throw new RuntimeException("외부 API 호출 중 오류가 발생했습니다");
            }

            // 1-5초 사이의 랜덤한 지연시간
            int delaySeconds = random.nextInt(5) + 1; // 1부터 5까지
            Thread.sleep(delaySeconds * 1000);

            // 성공적인 응답 시뮬레이션
            return "모델 " + model + "로부터의 응답: " + query + "에 대한 답변입니다.";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("호출이 중단되었습니다", e);
        }
    }
}
