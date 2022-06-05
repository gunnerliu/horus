package cn.archliu.horus.server.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Value("")
    private List<String> authPaths;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityInterceptor()).addPathPatterns("/**").excludePathPatterns("/login");
    }

}
