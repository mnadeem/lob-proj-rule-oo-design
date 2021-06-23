package com.org.lob.project.service.parser.expression;

import java.util.stream.Stream;

import org.springframework.util.StringUtils;

public class RuleExpression {

	private String path;
	private String subPath;
	private String tag;

	private Operator operator;

	private String value;

	private ReturnType returnType;

	public RuleExpression(String path, String subPath, String tag, Operator operator, String value, ReturnType returnType) {
		this.path = path;
		this.subPath = subPath;
		this.tag = tag;
		this.operator = operator;
		this.value = value;
		this.returnType = returnType;
	}

	public boolean valueMatches(String val) {
		if (operator.isEqual()) {
			return val != null && value.equalsIgnoreCase(val);
		} else if (operator.isNotEqual()) {
			return !value.equalsIgnoreCase(val);
		} else if (operator.isList()) {
			String[] tokenized = StringUtils.tokenizeToStringArray(value, "|");
			return Stream.of(tokenized).anyMatch(x -> x.equalsIgnoreCase(val));
		} else if (operator.isNotNull()) {
			return value != null;
		} else if (operator.isNull()) {
			return value == null;
		} else {		
			return false;
		}
	}

	public String getFullPath() {
		StringBuilder builder = new StringBuilder();

		if (StringUtils.hasText(path)) {
			builder.append(path);
		}

		if (StringUtils.hasText(subPath)) {
			builder.append(subPath);
		}

		if (StringUtils.hasText(tag)) {
			builder.append(tag);
		}

		return builder.toString();
	}

	public String getFullSubPath() {
		StringBuilder builder = new StringBuilder();

		if (StringUtils.hasText(subPath)) {
			builder.append(subPath);
		}

		if (StringUtils.hasText(tag)) {
			builder.append(tag);
		}

		return builder.toString();
	}

	public String getPath() {
		return path;
	}

	public String getSubPath() {
		return subPath;
	}

	public String getTag() {
		return tag;
	}

	public Operator getOperator() {
		return operator;
	}

	public String getValue() {
		return value;
	}

	public ReturnType getReturnType() {
		return returnType;
	}

	public static Path builder() {
        return new Builder();
    }

	public static class Builder implements Path, SubPath, Tag, OperatorStr, Val, ReturnTypeStr, Build {

		private String path;
		private String subPath;
		private String tag;
		private Operator operator;
		private String value;
		private ReturnType returnType;

		@Override
		public Build returnType(String returnType) {
			if (returnType != null) {				
				this.returnType = ReturnType.fromValue(returnType);
			}
			return this;
		}

		@Override
		public ReturnTypeStr value(String value) {
			this.value = value;
			return this;
		}

		@Override
		public Val operator(String operator) {
			this.operator = Operator.fromValue(operator);
			return this;
		}

		@Override
		public OperatorStr tag(String tag) {
			this.tag = tag;
			return this;
		}

		@Override
		public Tag subPath(String subPath) {
			this.subPath = subPath;
			return this;
		}

		@Override
		public SubPath path(String path) {
			this.path = path;
			return this;
		}

		@Override
		public RuleExpression build() {			
			return new RuleExpression(path, subPath, tag, operator, value, returnType);
		}		
	}

	public interface Path {
		SubPath path(String path);
	}
	
	public interface SubPath {
		Tag subPath(String subPath);
	}

	public interface Tag {
		OperatorStr tag(String tag);
	}

	public interface OperatorStr {
		Val operator(String operator);
	}

	public interface Val {
		ReturnTypeStr value(String value);
	}

	public interface ReturnTypeStr {
		Build returnType(String returnType);
	}

	public interface Build {
		RuleExpression build();
    }

	@Override
	public String toString() {
		return "MainExpression [path=" + path + ", subPath=" + subPath + ", tag=" + tag + ", operator=" + operator
				+ ", value=" + value + ", returnType=" + returnType + "]";
	}
}
