package com.org.lob.api.interceptor;

import static com.org.lob.support.Constants.REQUEST_PARAM_PROJECT;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.org.lob.project.service.RuleService;

@Component
public class UpdateCacheInterceptor implements AsyncHandlerInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateCacheInterceptor.class);

	private final RuleService ruleService;

	public UpdateCacheInterceptor(@NonNull RuleService ruleService) {
		this.ruleService = ruleService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String key = request.getParameter(REQUEST_PARAM_PROJECT);

		LOGGER.debug("Loading ... {}", key);
		ruleService.load(key);
		LOGGER.debug("Loaded ... {}", key);

		return true;
	}
}
