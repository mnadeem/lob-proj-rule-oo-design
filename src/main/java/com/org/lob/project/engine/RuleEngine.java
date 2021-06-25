package com.org.lob.project.engine;

import com.org.lob.project.engine.expression.Expressions;

public interface RuleEngine {
	String evaluate(Expressions expressions) throws Exception;
}
