<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans 
		http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">
	
	<#if models?exists && models?size != 0>
	<#list models as model>
	<bean id="${firstToLower(model.className)}DAO" class="${daoImplPackage}.${model.className}DAOImpl">
		<property name="sqlSessionTemplate" ref="sqlSession"/>
	</bean>
	</#list>
	</#if>
	
</beans>