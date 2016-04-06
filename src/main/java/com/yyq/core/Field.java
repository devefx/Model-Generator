package com.yyq.core;

public class Field {
	
	private String jdbcName;
	private String javaName;
	
	private String jdbcType;
	private String javaType;
	
	private String comment;
	
	public String getJdbcName() {
		return jdbcName;
	}
	public void setJdbcName(String jdbcName) {
		this.jdbcName = jdbcName;
	}
	public String getJavaName() {
		return javaName;
	}
	public void setJavaName(String javaName) {
		this.javaName = javaName;
	}
	public String getJdbcType() {
		return jdbcType;
	}
	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}
	public String getJavaType() {
		return javaType;
	}
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
