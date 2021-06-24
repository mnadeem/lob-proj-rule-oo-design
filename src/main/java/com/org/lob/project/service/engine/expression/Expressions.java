package com.org.lob.project.service.engine.expression;

import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

public class Expressions {

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

	public String buildExpression() {
		StringBuilder builder = new StringBuilder();
		builder.append(getMainPath()).append(main.getSubExpression());
		return builder.toString();
	}

	public String getMainPath() {
		return this.main.getPath();
	}

	public boolean isOnlyMain() {
		return main != null && CollectionUtils.isEmpty(ors) && CollectionUtils.isEmpty(ands);
	}

	public boolean isOrsPresent() {
		return !CollectionUtils.isEmpty(ors);
	}

	public boolean isAndsPresent() {
		return !CollectionUtils.isEmpty(ands);
	}

	public boolean isBothPresent() {
		return isOrsPresent() && isAndsPresent();
	}

	public boolean isXmlValueReturnType() {
		return returnExpression != null && ReturnType.XML_VAL == returnExpression.getReturnType();
	}

	public boolean isTrueReturnType() {
		return returnExpression != null && ReturnType.TRUE == returnExpression.getReturnType();
	}

	public boolean isFalseReturnType() {
		return returnExpression != null && ReturnType.FALSE == returnExpression.getReturnType();
	}

	public boolean isBoolReturnType() {
		return isTrueReturnType() || isFalseReturnType();
	}

	public RuleExpression getMain() {
		return main;
	}

	public List<RuleExpression> getOrs() {
		return ors;
	}

	public List<RuleExpression> getAnds() {
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
			this.ands.add(andExpression);
			return this;
		}

		@Override
		public Or or(RuleExpression orExpression) {
			this.ors.add(orExpression);
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
