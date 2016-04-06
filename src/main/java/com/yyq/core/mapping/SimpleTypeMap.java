package com.yyq.core.mapping;

public class SimpleTypeMap extends TypeMap {

	@Override
	public void init() {
		// 数值类型
		set("tinyint", "byte");
		set("smallint", "short");
		set("mediumint", "int");
		set("int", "int");
		set("bigint", "long");
		set("float", "float");
		set("double", "double");
		set("decimal", "java.math.BigDecimal");
		// 字符串类型 
		set("char", "String");
		set("varchar", "String");
		set("tinyblob", "String");
		set("blob", "String");
		set("mediumblob", "String");
		set("longblob", "String");
		set("tinytext", "String");
		set("text", "String");
		set("mediumtext", "String");
		set("longtext", "String");
		set("enum", "");
		set("set", "");
		// 日期时间类型
		set("date", "java.util.Date");
		set("time", "java.util.Date");
		set("datetime", "java.util.Date");
		set("timestamp", "java.util.Date");
		set("year", "java.util.Date");
	}
	
}
