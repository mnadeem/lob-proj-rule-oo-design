package com.org.lob.project.service.engine.expression;

public class ReturnExpression {

	private final String subPath;
	private final String tag;
	private final ReturnType returnType;

	public ReturnExpression(String subPath, String tag, ReturnType returnType) {
		this.subPath = subPath;
		this.tag = tag;
		this.returnType = returnType;
	}

	public static SubPath builder() {
        return new Builder();
    }

	public static class Builder implements SubPath, Tag, ReturnTypeStr, Build {

		private String subPath;
		private String tag;
		private ReturnType returnType;

		@Override
		public Tag subPath(String subPath) {
			this.subPath = subPath;
			return this;
		}

		@Override
		public ReturnTypeStr tag(String tag) {
			this.tag = tag;
			return this;
		}

		@Override
		public Build returnType(String returnType) {
			if (returnType != null) {				
				this.returnType = ReturnType.fromValue(returnType);
			}
			return this;
		}

		@Override
		public ReturnExpression build() {
			return new ReturnExpression(subPath, tag, returnType);
		}

	}

	public String getSubPath() {
		return subPath;
	}

	public String getTag() {
		return tag;
	}

	public ReturnType getReturnType() {
		return returnType;
	}
	
	
	public interface SubPath {
		Tag subPath(String subPath);
	}

	public interface Tag {
		ReturnTypeStr tag(String tag);
	}

	public interface ReturnTypeStr {
		Build returnType(String returnType);
	}

	public interface Build {
		ReturnExpression build();
    }

	@Override
	public String toString() {
		return "ReturnExpression [subPath=" + subPath + ", tag=" + tag + ", returnType=" + returnType + "]";
	}

}
