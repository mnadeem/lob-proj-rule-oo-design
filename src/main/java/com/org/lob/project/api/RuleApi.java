package com.org.lob.project.api;

import static com.org.lob.support.Constants.REQUEST_MAPPING_RULE;
import static com.org.lob.support.Constants.REQUEST_PARAM_IDS;
import static com.org.lob.support.Constants.REQUEST_PARAM_PROJECT;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.lob.project.service.RuleService;

@RestController
@RequestMapping(REQUEST_MAPPING_RULE)
public class RuleApi {

	private final RuleService ruleService;

	public RuleApi(RuleService ruleService) {
		this.ruleService = ruleService;
	}

	// GET : ?ids=1&ids=2&ids=3&project=./src/main/resources/employees.xml
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getByIds(@RequestParam(name = REQUEST_PARAM_PROJECT, required = true) String project, @RequestParam(name = REQUEST_PARAM_IDS, required = true) List<Long> ids) {
		return ResponseEntity.ok(ruleService.execute(project, ids));
	}
}
