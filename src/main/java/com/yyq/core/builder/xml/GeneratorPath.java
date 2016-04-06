package com.yyq.core.builder.xml;

public class GeneratorPath {
	private String targetPackage;
	private String targetProject;
	
	public GeneratorPath(String targetPackage, String targetProject) {
		this.targetPackage = targetPackage;
		this.targetProject = targetProject;
	}
	
	public String getTargetPackage() {
		return targetPackage;
	}
	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
	}
	public String getTargetProject() {
		return targetProject;
	}
	public void setTargetProject(String targetProject) {
		this.targetProject = targetProject;
	}
}
