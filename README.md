# 🧩 Assignment Project

Spring Boot 기반의 **User / Assistant 도메인 서비스** 구현 및 단위 테스트 프로젝트입니다.<br>
사용자 정보 관리, 모델 질의 처리, 토큰 사용량 계산 및 예외 처리를 포함하고 있습니다.

---

# 📁 도메인 설계 및 분리 이유

이 프로젝트는 실제 서비스 환경에서의 **트래픽 특성**과 확장성을 고려해 `user`와 `assistant` 도메인을 분리했습니다.
- User 도메인
    - 사용자 등록, 요금제, 토큰 사용량 관리 등 상대적으로 요청 빈도가 낮고 데이터 정합성이 중요한 영역입니다.
    - 필터를 통해 인증된 사용자의 `userId`가 헤더에 자동 주입되어 각 요청에 포함되며, 서비스 로직은 해당 ID를 기반으로 동작합니다.
- Assistant 도메인
    - LLM 질의 및 응답 처리를 담당하며, 트래픽이 집중되는 구간입니다.
    - 모델 호출, 응답 파싱, 토큰 차감 등 실시간 연산이 빈번하게 발생하므로 **Redis 캐시 및 비동기 처리 구조**를 고려했습니다.
    - 실제 환경에서는 이 부분이 AI 요청 처리 서버로 독립될 가능성이 높아, 도메인 레벨에서 명확히 분리해두었습니다.

이 분리 구조를 통해 트래픽 집중 구간(assistant)과 데이터 관리 구간(user)을 독립적으로 확장하거나 배포할 수 있습니다.

---

# ⚙️ 기술 스택

- 언어: Java 21
- 프레임워크: Spring Boot 3.5.5
- ORM: JPA
- 빌드 도구: Gradle
- 데이터베이스: H2 (Test), MariaDB
- 캐시/세션 관리: Redis
- 테스트: JUnit 5, Mockito, AssertJ
- 로깅: SLF4J + Logback

---

# 🧠 Redis 사용 이유

1. 남은 토큰 수 관리 (Remaining Tokens Cache)
    - 사용자별 토큰 잔량을 빠르게 조회하고 업데이트하기 위해 Redis를 사용합니다.
    - DB 접근 없이 실시간 응답이 가능하며, TTL 설정을 통해 자동 만료 관리가 가능합니다.
2. 트래픽 부하 분산
    - LLM 질의 요청이 집중될 때 Redis를 통해 사용자 상태 및 요청 이력을 공유해 부하를 완화합니다.

### 🚀 Redis 실행 명령어

[bash]
docker run --name assignment-redis -p 6379:6379 -d redis

### 실행 확인
docker ps | grep redis

---

# 🧪 단위 테스트 실행

테스트는 Service 단위로 작성되어 있습니다.

`UserServiceTest` → 사용자 생성 및 사용량 관리
`AssistantServiceTest` → 질의 처리 및 예외 상황 검증

### ▶ User 도메인 테스트
> --
[bash]
./gradlew test --tests "com.posicube.assignment.domain.user.UserServiceTest" --rerun-tasks

### ▶ Assistant 도메인 테스트
> --
[bash]
./gradlew test --tests "com.posicube.assignment.domain.assistant.AssistantServiceTest" --rerun-tasks

rerun-tasks 옵션을 통해 캐시된 테스트 결과를 무시하고 매번 새로 실행합니다.

---

# 🚨 에러 처리 구조

모든 예외는 공통 예외 클래스(`ApiCommonException`)를 통해 관리됩니다.<br>
`GlobalErrorCode` Enum에 정의된 코드로 일관된 에러 응답을 제공합니다.

### 주요 에러 코드

| 코드 | 설명 |
| --- | --- |
| `NOT_FOUND_USER` | 유저 정보 없음 |
| `CALL_ABORT` | LLM 질의 중단 또는 실패 |
| `INVALID_PARAM` | 요청 파라미터 오류 |

---

# 🧩 테스트 로그 출력 설정

`build.gradle` 하단에 아래 설정을 추가하면 터미널에서 테스트 로그가 실시간으로 출력됩니다.
> --
tasks.withType(Test) {
    useJUnitPlatform()
    testLogging {
        events "passed", "skipped", "failed"
        showStandardStreams = true
    }
}

---

# ✅ 실행 결과 예시
> --
UserServiceTest > 유저 저장 성공 PASSED<br>
UserServiceTest > 사용 내역 조회 성공 - PRO 요금제 PASSED<br>
AssistantServiceTest > 질의 성공 - 정상 응답 반환 PASSED<br>
AssistantServiceTest > 질의 실패 - LLM 예외 발생 시 ApiCommonException 반환 PASSED
