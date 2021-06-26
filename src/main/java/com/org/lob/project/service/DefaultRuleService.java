package com.org.lob.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.org.lob.project.engine.DefaultRuleEngine;
import com.org.lob.project.engine.RuleEngine;
import com.org.lob.project.engine.RuleEngineResult;
import com.org.lob.project.engine.expression.Expressions;
import com.org.lob.project.engine.expression.Operator;
import com.org.lob.project.engine.expression.RuleExpression;
import com.org.lob.project.repository.FileRepository;

@Service
public class DefaultRuleService implements RuleService {

	private final FileRepository fileRepository;

	public DefaultRuleService(FileRepository fileRepository) {
		this.fileRepository = fileRepository;
	}

	@Override
	public String load(String project) {
		return fileRepository.load(project);
	}

	@Override
	public String reLoad(String project) {
		return fileRepository.reLoad(project);
	}
	
	@Override
	public List<RuleEngineResult> execute(String project, List<Long> ids) {

		RuleEngine engine = ruleEngine(project);

		return engine.evaluate(buildExpressions(ids));
	}

	private RuleEngine ruleEngine(String project) {
		return new DefaultRuleEngine(fileRepository.load(project));
	}

	private List<Expressions> buildExpressions(List<Long> ids) {
		List<Expressions> result = new ArrayList<>();
		for (Long id : ids) {
			result.add(buildExpression(id));
		}
		return result;
	}

	private Expressions buildExpression(Long ruleId) {
		// Load rule from DB
		return Expressions
				.builder(ruleId, RuleExpression
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
