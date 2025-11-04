package com.posicube.assignment.filter;

import com.posicube.assignment.common.dto.CommonParamDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CommonParamFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String userIdHeader = request.getHeader("X-User-Id");

        Long userId = null;

        if (userIdHeader != null) {
            try {
                userId = Long.parseLong(userIdHeader);
            } catch (NumberFormatException ignored) {}
        }

        CommonParamDto commonParamDto = new CommonParamDto(request, userId);

        // 요청 범위(request scope)에 저장해서 Controller에서 사용 가능하도록 설정
        request.setAttribute("commonParamDto", commonParamDto);

        filterChain.doFilter(request, response);
    }
}
