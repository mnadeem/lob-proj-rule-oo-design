package com.org.lob.project.service;

import java.util.List;

import com.org.lob.project.engine.RuleEngineResult;

public interface RuleService {
	
	String load(String project);

	String reLoad(String project);
	
	List<RuleEngineResult> execute(String project, List<Long> ids);
}
