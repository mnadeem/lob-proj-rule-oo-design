package com.org.lob.project.service.engine.expression;

import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

public class Expressions {

	private static final String CONJUCTION_AND = "and";

	private static final char PARAN_END = ')';
	private static final char PARAN_START = '(';
	private static final char SEPERATOR_SPACE = ' ';
	private static final char BRACKET_END = ']';
	private static final char BRACKET_START = '[';

	private final RuleExpression main;
	private final List<RuleExpression> ands;
	private final List<RuleExpression> ors;
	private final ReturnExpression returnExpression;

	private Expressions(RuleExpression main, List<RuleExpression> ors, List<RuleExpression> ands, ReturnExpression returnExpression) {
		this.main = main;
		this.ors = ors;
		this.ands = ands;
		this.returnExpression = returnExpression;
	}

	public String buildEvaluationExpression() {
		StringBuilder builder = new StringBuilder();

		builder.append(getMainPath())
			    .append(BRACKET_START);

		builder.append(main.getSubExpression());

		if (isAndsPresent()) {
			builder.append(SEPERATOR_SPACE)
					.append(CONJUCTION_AND)
					.append(SEPERATOR_SPACE)
					.append(PARAN_START);

			builder.append(getAnds().stream().map(and -> and.getSubExpression()).collect(Collectors.joining(" and ")));

			builder.append(PARAN_END);
		}

		if (isOrsPresent()) {
			builder.append(SEPERATOR_SPACE)
					.append(CONJUCTION_AND)
					.append(SEPERATOR_SPACE)
					.append(PARAN_START);

			builder.append(getOrs().stream().map(or -> or.getSubExpression()).collect(Collectors.joining(" or ")));

			builder.append(PARAN_END);
		}

		builder.append(BRACKET_END);

		return builder.toString();
	}

	public String buildReturnExpression() {
		if (isXmlValueReturnType()) {
			return returnExpression.getExpression(getMainPath());
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

	private boolean isXmlValueReturnType() {
		return returnExpression != null && ReturnType.XML_VAL == returnExpression.getReturnType();
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

	public static And builder(@NonNull RuleExpression main) {
		notNull(main, "main should not be null");
        return new Builder(main);
    }

	public static class Builder implements And, Or, ReturnExpressionStr, Build {

		private RuleExpression main;
		private List<RuleExpression> ors = new ArrayList<>();
		private List<RuleExpression> ands = new ArrayList<>();
		
		private ReturnExpression returnExpression;

		public Builder(RuleExpression main) {
			notNull(main, "main should not be null");
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
			return new Expressions(main, ors, ands, returnExpression);
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
