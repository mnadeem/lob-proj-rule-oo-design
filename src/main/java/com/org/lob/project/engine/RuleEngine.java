package com.org.lob.project.engine;

import java.util.List;

import com.org.lob.project.engine.expression.Expressions;

public interface RuleEngine {

	RuleEngineResult evaluate(Expressions expressions);
	
	List<RuleEngineResult> evaluate(List<Expressions> expressions);
}
