package com.org.lob.project.engine.expression;

import static com.org.lob.support.Constants.BRACKET_END;
import static com.org.lob.support.Constants.BRACKET_START;
import static com.org.lob.support.Constants.CONJUNCTION_AND;
import static com.org.lob.support.Constants.CONJUNCTION_OR;
import static com.org.lob.support.Constants.PARENTHESES_END;
import static com.org.lob.support.Constants.PARENTHESES_START;
import static com.org.lob.support.Constants.SEPERATOR_SPACE;
import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

public class Expressions {
	private final Long ruleId;
	private final RuleExpression main;
	private final List<RuleExpression> ands;
	private final List<RuleExpression> ors;
	private final ReturnExpression returnExpression;

	private Expressions(Long ruleId, RuleExpression main, List<RuleExpression> ors, List<RuleExpression> ands, ReturnExpression returnExpression) {
		this.ruleId = ruleId;
		this.main = main;
		this.ors = ors;
		this.ands = ands;
		this.returnExpression = returnExpression;
	}

	public String buildEvaluationExpression() {
		StringBuilder builder = new StringBuilder();

		builder.append(getMainPath())
			    .append(BRACKET_START);

		addMainExpression(builder);

		addAndExpressions(builder);

		addOrsExpressions(builder);

		builder.append(BRACKET_END);

		return builder.toString();
	}

	private void addMainExpression(StringBuilder builder) {
		builder.append(PARENTHESES_START)
				.append(main.getSubExpression())
				.append(PARENTHESES_END);
	}

	private void addOrsExpressions(StringBuilder builder) {
		if (isOrsPresent()) {
			builder.append(SEPERATOR_SPACE)
					.append(CONJUNCTION_AND)
					.append(SEPERATOR_SPACE)
					.append(PARENTHESES_START);

			builder.append(getOrs().stream().map(or -> or.getSubExpression()).collect(Collectors.joining(SEPERATOR_SPACE + CONJUNCTION_OR + SEPERATOR_SPACE )));

			builder.append(PARENTHESES_END);
		}
	}

	private void addAndExpressions(StringBuilder builder) {
		if (isAndsPresent()) {
			builder.append(SEPERATOR_SPACE)
					.append(CONJUNCTION_AND)
					.append(SEPERATOR_SPACE)
					.append(PARENTHESES_START);

			builder.append(getAnds().stream().map(and -> and.getSubExpression()).collect(Collectors.joining(SEPERATOR_SPACE + CONJUNCTION_AND + SEPERATOR_SPACE)));

			builder.append(PARENTHESES_END);
		}
	}

	public String buildReturnExpression() {
		if (isXmlValueReturnType()) {
			return returnExpression.getExpression();
		}
		return null;
	}

	private String getMainPath() {
		return this.getMain().getPath();
	}

	private boolean isOrsPresent() {
		return !CollectionUtils.isEmpty(ors);
	}

	private boolean isAndsPresent() {
		return !CollectionUtils.isEmpty(ands);
	}

	public boolean isXmlValueReturnType() {
		return returnExpression != null && returnExpression.isXmlValueReturnType();
	}

	public ReturnType getReturnType() {
		return returnExpression != null ? returnExpression.getReturnType() : null;
	}

	private RuleExpression getMain() {
		return main;
	}

	private List<RuleExpression> getOrs() {
		return ors;
	}

	private List<RuleExpression> getAnds() {
		return ands;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public static And builder(Long ruleId, RuleExpression main) {
		notNull(main, "main should not be null");
        return new Builder(ruleId, main);
    }

	public static class Builder implements And, Or, ReturnExpressionStr, Build {
		private Long ruleId;
		private RuleExpression main;
		private List<RuleExpression> ors = new ArrayList<>();
		private List<RuleExpression> ands = new ArrayList<>();
		
		private ReturnExpression returnExpression;

		public Builder(Long ruleId, RuleExpression main) {
			notNull(main, "main should not be null");
			this.ruleId = ruleId;
			this.main = main;
		}

		@Override
		public And and(RuleExpression andExpression) {
			if (andExpression != null) {				
				this.ands.add(andExpression);
			}
			return this;
		}

		@Override
		public Or or(RuleExpression orExpression) {
			if (orExpression != null) {				
				this.ors.add(orExpression);
			}
			return this;
		}

		@Override
		public Build returnExpression(ReturnExpression returnExpression) {
			this.returnExpression = returnExpression;
			return this;
		}

		@Override
		public Expressions build() {
			return new Expressions(ruleId, main, ors, ands, returnExpression);
		}		
	}

	public interface And {
		And and(RuleExpression andExpression);
		Or or(RuleExpression orExpression);
		Build returnExpression(ReturnExpression returnExpression);
		Expressions build();
	}

	public interface Or {
		Or or(RuleExpression orExpression);
		Build returnExpression(ReturnExpression returnExpression);
		Expressions build();
	}

	public interface ReturnExpressionStr {
		Build returnExpression(ReturnExpression returnExpression);
	}

	public interface Build {
		Expressions build();
    }

	@Override
	public String toString() {
		return "Expressions [main=" + main + ", ors=" + ors + ", ands=" + ands + ", returnExpression=" + returnExpression + "]";
	}
}
