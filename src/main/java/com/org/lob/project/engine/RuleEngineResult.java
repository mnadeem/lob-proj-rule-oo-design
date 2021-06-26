package com.org.lob.project.engine;

import com.org.lob.project.engine.expression.ReturnType;

public class RuleEngineResult {

	private Long id;
	private String result;
	private ReturnType returnType;

	public RuleEngineResult(Long id, String result, ReturnType returnType) {
		this.id = id;
		this.result = result;
		this.returnType = returnType;
	}

	public Long getId() {
		return id;
	}

	public String getResult() {
		return result;
	}

	public ReturnType getReturnType() {
		return returnType;
	}

	@Override
	public String toString() {
		return String.format("RuleEngineResult [id=%s, result=%s, returnType=%s]", id, result, returnType);
	}
}
