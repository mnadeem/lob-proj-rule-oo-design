package com.org.lob.project.service.parser.expression;

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
