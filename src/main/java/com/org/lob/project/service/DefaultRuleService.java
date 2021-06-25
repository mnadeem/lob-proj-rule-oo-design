package com.org.lob.project.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.org.lob.project.engine.DefaultRuleEngine;
import com.org.lob.project.engine.RuleEngine;
import com.org.lob.project.engine.expression.Expressions;
import com.org.lob.project.engine.expression.Operator;
import com.org.lob.project.engine.expression.RuleExpression;

@Service
@CacheConfig(cacheNames = "rules")
public class DefaultRuleService implements RuleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRuleService.class);

	@Override
	@Cacheable
	public String load(String project) {
		return asString(new FileSystemResource(project));
	}

	private String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
        	LOGGER.error("Error Proessing ", e);
            throw new UncheckedIOException(e);
        }
    }

	@Override
	@CachePut
	public String reLoad(String project) {
		return asString(new FileSystemResource(project));
	}

	@Override
	public List<String> execute(String project, List<Long> ids) {
		List<String> result = new ArrayList<>();

		for (Long ruleId : ids) {
			Expressions expressions = buildExpressions(ruleId);
			result.add(ruleEngine(project).evaluate(expressions));
		}
		return result;
	}

	private RuleEngine ruleEngine(String project) {
		return new DefaultRuleEngine(load(project));
	}

	private Expressions buildExpressions(Long ruleId) {
		// Load rule from DB
		return Expressions
				.builder(RuleExpression
						.builder()
						.path("/employees/employee")
						//.subPath(null)
						.tag("firstName")
						.operatorBuild("Is Not Null")
						.build()
					)
				.and(RuleExpression
						.subPathBuilder()
						//.subPath(null)
						.tag("department/id")
						.operator(Operator.EQUAL)
						.value("10100")
						.build()
					)
			.build();
	}
}
