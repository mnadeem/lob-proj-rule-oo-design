package com.org.lob.project.service;

import java.util.List;

public interface RuleService {

	String load(String project);
	
	String reLoad(String project);
	
	List<String> execute(String project, List<Long> ids);
}
