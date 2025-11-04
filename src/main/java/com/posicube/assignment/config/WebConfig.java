package com.posicube.assignment.config;

import com.posicube.assignment.filter.CommonParamArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CommonParamArgumentResolver commonParamArgumentResolver;

    public WebConfig(CommonParamArgumentResolver commonParamArgumentResolver) {
        this.commonParamArgumentResolver = commonParamArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(commonParamArgumentResolver);
    }
}
