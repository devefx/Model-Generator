package com.yyq.core.mapping;

import java.util.HashMap;
import java.util.Map;

public abstract class TypeMap {
	
	private Map<String, String> jdbcTojava;
	
	public TypeMap() {
		jdbcTojava = new HashMap<String, String>();
		init();
	}
	
	public void set(String jdbcType, String javaType) {
		jdbcTojava.put(jdbcType, javaType);
	}
	
	public String get(String jdbcType) {
		return jdbcTojava.get(jdbcType);
	}
	
	public abstract void init();
}
