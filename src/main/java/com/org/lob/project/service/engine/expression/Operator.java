package com.org.lob.project.service.engine.expression;

public enum Operator {
	EQUAL("Equal"), NOT_EQUAL("Not Equal"), LIST("List"), NULL("Is Null"), NOT_NULL("Is Not Null");

	private final String value;

	private Operator(String v) {
        this.value = v;
    }

	public boolean isEqual() {
		return this == EQUAL;
	}

	public boolean isNotEqual() {
		return this == NOT_EQUAL;
	}

	public boolean isList() {
		return this == LIST;
	}

	public boolean isNull() {
		return this == NULL;
	}

	public boolean isNotNull() {
		return this == NOT_NULL;
	}

	public static Operator fromValue(String v) {
        for (Operator c: Operator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v + " Not Recognized!");
    }
}
