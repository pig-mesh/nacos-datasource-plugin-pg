package com.pig4cloud.plugin.impl.postgresql;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lengleng
 * @date 2024/8/25
 */
public enum TrustedPgFunctionEnum {

	/**
	 * NOW().
	 */
	NOW("NOW()", "NOW()");

	private static final Map<String, TrustedPgFunctionEnum> LOOKUP_MAP = new HashMap<>();

	static {
		for (TrustedPgFunctionEnum entry : TrustedPgFunctionEnum.values()) {
			LOOKUP_MAP.put(entry.functionName, entry);
		}
	}

	private final String functionName;

	private final String function;

	TrustedPgFunctionEnum(String functionName, String function) {
		this.functionName = functionName;
		this.function = function;
	}

	/**
	 * Get the function name.
	 * @param functionName function name
	 * @return function
	 */
	public static String getFunctionByName(String functionName) {
		TrustedPgFunctionEnum entry = LOOKUP_MAP.get(functionName);
		if (entry != null) {
			return entry.function;
		}
		throw new IllegalArgumentException(String.format("Invalid function name: %s", functionName));
	}

}
