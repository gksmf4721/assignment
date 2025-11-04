package com.posicube.assignment.filter;

import com.posicube.assignment.common.dto.CommonParamDto;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controller에서 다음과 같이 주입받을 수 있음:
 * public ResponseEntity<?> getData(CommonParamDto common)
 */
@Component
public class CommonParamArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return CommonParamDto.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        // 예시: 헤더나 요청 파라미터에서 공통 값 추출
        String userId = request.getHeader("X-User-Id");
        CommonParamDto commonParamDto = new CommonParamDto();

        if (userId != null) {
            commonParamDto.setUserId(Long.parseLong(userId));
        }

        return commonParamDto;
    }
}
