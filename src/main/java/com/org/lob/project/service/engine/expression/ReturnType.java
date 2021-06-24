package com.org.lob.project.service.engine.expression;

public enum ReturnType {
	TRUE("TRUE"), FALSE("FALSE"), XML_VAL("XML value");

	private final String value;

	private ReturnType(String v) {
        this.value = v;
    }

	public static ReturnType fromValue(String v) {
        for (ReturnType c: ReturnType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v + " Not Recognized!");
    }
}
