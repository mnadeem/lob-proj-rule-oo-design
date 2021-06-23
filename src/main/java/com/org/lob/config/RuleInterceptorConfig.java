package com.org.lob.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.org.lob.api.interceptor.UpdateCacheInterceptor;

@Configuration
public class RuleInterceptorConfig implements WebMvcConfigurer {

	private final UpdateCacheInterceptor updateCacheInterceptor;

	public RuleInterceptorConfig(UpdateCacheInterceptor updateCacheInterceptor) {
		this.updateCacheInterceptor = updateCacheInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(updateCacheInterceptor);
	}
}
