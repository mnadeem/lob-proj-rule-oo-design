package com.org.lob.project.service.parser.expression;

import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;

public class Expressions {

	private RuleExpression main;
	private List<RuleExpression> ors;
	private List<RuleExpression> ands;

	private ReturnType returnType;

	private Expressions(RuleExpression main, List<RuleExpression> ors, List<RuleExpression> ands, ReturnType returnType) {
		this.main = main;
		this.ors = ors;
		this.ands = ands;
		this.returnType = returnType;
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

	public ReturnType getReturnType() {
		return returnType;
	}

	public static And builder(@NonNull RuleExpression main) {
		notNull(main, "main should not be null");
        return new Builder(main);
    }

	public static class Builder implements And, Or {

		private RuleExpression main;
		private List<RuleExpression> ors = new ArrayList<>();
		private List<RuleExpression> ands = new ArrayList<>();

		private ReturnType returnType;

		public Builder(RuleExpression main) {
			this.main = main;
		}

		@Override
		public And and(@NonNull RuleExpression andExpression) {
			notNull(andExpression, "andExpression should not be null");
			this.ands.add(andExpression);

			if (andExpression.getReturnType() != null && this.returnType == null) {
				this.returnType = andExpression.getReturnType();
			}
			return this;
		}

		@Override
		public Or or(@NonNull RuleExpression orExpression) {
			notNull(orExpression, "orExpression should not be null");
			this.ors.add(orExpression);
			if (orExpression.getReturnType() != null && this.returnType == null) {
				this.returnType = orExpression.getReturnType();
			}
			return this;
		}

		@Override
		public Expressions build() {
			return new Expressions(main, ors, ands, returnType);
		}		
	}

	public interface And {
		And and(RuleExpression andExpression);
		Or or(RuleExpression orExpression);
	}

	public interface Or {
		Or or(RuleExpression orExpression);
		Expressions build();		
	}

	@Override
	public String toString() {
		return "Expressions [main=" + main + ", ors=" + ors + ", ands=" + ands + ", returnType=" + returnType + "]";
	}
}
