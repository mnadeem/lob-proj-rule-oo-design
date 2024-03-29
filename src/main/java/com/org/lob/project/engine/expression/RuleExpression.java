package com.org.lob.project.engine.expression;

import static com.org.lob.support.Constants.CONJUNCTION_NOT;
import static com.org.lob.support.Constants.CONJUNCTION_OR;
import static com.org.lob.support.Constants.EQUAL;
import static com.org.lob.support.Constants.PARENTHESES_END;
import static com.org.lob.support.Constants.PARENTHESES_START;
import static com.org.lob.support.Constants.QUOTE_END;
import static com.org.lob.support.Constants.QUOTE_START;
import static com.org.lob.support.Constants.SEPERATOR_PIPE;
import static com.org.lob.support.Constants.SEPERATOR_SPACE;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.StringUtils;

public class RuleExpression {

	private final String path;
	private final String subPath;
	private final String tag;

	private final Operator operator;

	private final String value;

	public RuleExpression(String path, String subPath, String tag, Operator operator, String value) {
		this.path = path;
		this.subPath = subPath;
		this.tag = tag;
		this.operator = operator;
		this.value = value;
	}

	public String getSubExpression() {
		StringBuilder builder = new StringBuilder();

		if (StringUtils.hasText(subPath)) {
			builder.append(subPath);
		}

		if (operator.isEqual()) {
			appendEqualsExpression(builder);
		} else if (operator.isNotEqual()) {
			appendNotEqualsExpression(builder);
		} else if (operator.isList()) {
			appendListExpression(builder);

		} else if (operator.isNotNull()) {
			appendElementExists(builder);
			
		} else if (operator.isNull()) {
			appendElementDoesNotExists(builder);
		}

		return builder.toString();
	}

	private void appendElementDoesNotExists(StringBuilder builder) {

		builder.append(CONJUNCTION_NOT)
				.append(PARENTHESES_START)
				.append(tag)
				.append(PARENTHESES_END);
	}

	private void appendElementExists(StringBuilder builder) {
		builder.append(tag);
	}

	private void appendListExpression(StringBuilder builder) {

		String[] tokenized = StringUtils.tokenizeToStringArray(value, SEPERATOR_PIPE);
		String result = Stream.of(tokenized).map(x -> tag + EQUAL + QUOTE_START + x + QUOTE_END).collect(Collectors.joining(SEPERATOR_SPACE + CONJUNCTION_OR + SEPERATOR_SPACE));
		builder.append(result);
	}

	private void appendNotEqualsExpression(StringBuilder builder) {
		builder.append(CONJUNCTION_NOT)
				.append(PARENTHESES_START)
				.append(tag)
				.append(EQUAL)
				.append(QUOTE_START)
				.append(value)
				.append(QUOTE_END)
				.append(PARENTHESES_END);
	}

	private void appendEqualsExpression(StringBuilder builder) {
		builder.append(tag)
				.append(EQUAL)
				.append(QUOTE_START)
				.append(value)
				.append(QUOTE_END);
	}

	public String getPath() {
		return path;
	}

	public static Path builder() {
        return new Builder();
    }
	
	public static SubPath subPathBuilder() {
        return new Builder();
    }

	public static class Builder implements Path, SubPath, Tag, OperatorStr, Val, Build {

		private String path;
		private String subPath;
		private String tag;
		private Operator operator;
		private String value;

		@Override
		public SubPath path(String path) {
			this.path = path;
			return this;
		}

		@Override
		public Tag subPath(String subPath) {
			this.subPath = subPath;
			return this;
		}
		

		@Override
		public OperatorStr tag(String tag) {
			this.tag = tag;
			return this;
		}
		
		@Override
		public Val operator(String operator) {
			this.operator = Operator.fromValue(operator);
			return this;
		}

		@Override
		public Val operator(Operator operator) {
			this.operator = operator;
			return this;
		}

		@Override
		public Build operatorBuild(String operator) {
			this.operator = Operator.fromValue(operator);
			return this;
		}

		@Override
		public Build value(String value) {
			this.value = value;
			return this;
		}

		@Override
		public RuleExpression build() {			
			return new RuleExpression(path, subPath, tag, operator, value);
		}

	}

	public interface Path {
		SubPath path(String path);
	}

	public interface SubPath {
		Tag subPath(String subPath);
		OperatorStr tag(String tag);
	}

	public interface Tag {
		OperatorStr tag(String tag);
	}

	public interface OperatorStr {
		Val operator(String operator);
		Val operator(Operator operator);
		Build operatorBuild(String operator);
	}

	public interface Val {
		Build value(String value);
	}

	public interface Build {
		RuleExpression build();
    }

	@Override
	public String toString() {
		return "MainExpression [path=" + path + ", subPath=" + subPath + ", tag=" + tag + ", operator=" + operator
				+ ", value=" + value + "]";
	}
}
