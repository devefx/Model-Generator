package com.yyq.core;

import java.util.List;

public class Model {
	
	private String tableName;
	private String className;
	private List<Field> fields;
	private Field key;

	public Model() {
	}
	
	public Model(String tableName, String className) {
		this.tableName = tableName;
		this.className = className;
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<Field> getFields() {
		return fields;
	}
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	public Field getKey() {
		return key;
	}
	public void setKey(Field key) {
		this.key = key;
	}
}
