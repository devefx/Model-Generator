<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${entityPackage}.${model.className}Mapper">
	
	<!--
		${model.className}Result
		@author YYQ Model-Generator
	 -->
	<resultMap id="${model.className}Result" type="${entityPackage}.${model.className}">
		<#if model.key??>
		<id column="${model.key.jdbcName}" property="${model.key.javaName}" />
		</#if>
		<#list model.fields as field>
		<result column="${field.jdbcName}" property="${field.javaName}" />
		</#list>
	</resultMap>
	
	<sql id="Base_Column_List" >
		<#list model.fields as field>${(field_index gt 0)?string(",", "")}${field.jdbcName}</#list>
	</sql>
	
	<#if model.key??>
	<select id="get${model.className}ById" parameterType="int" resultMap="${model.className}Result">
		select
		${model.key.jdbcName},<include refid="Base_Column_List" />
		from ${model.tableName}
		where ${model.key.jdbcName} = ${r"#{"}${model.key.javaName}${r"}"}
	</select>
	
	<delete id="delete${model.className}ById" parameterType="int" >
		delete from ${model.tableName}
		where ${model.key.jdbcName} = ${r"#{"}${model.key.javaName}${r"}"}
	</delete>
	
	<update id="updateBy${model.className}" parameterType="${entityPackage}.${model.className}" >
		update ${model.tableName} set 
		<#list model.fields as field>${(field_index gt 0)?string(",", "")}${field.jdbcName} = ${r"#{"}${field.javaName}${r"}"}</#list>
		where ${model.key.jdbcName} = ${r"#{"}${model.key.javaName}${r"}"}
	</update>
	</#if>
	
	<insert id="insert${model.className}" parameterType="${entityPackage}.${model.className}" >
		insert into ${model.tableName} (
		<#list model.fields as field>${(field_index gt 0)?string(",", "")}${field.jdbcName}</#list>
		) values (
		<#list model.fields as field>${(field_index gt 0)?string(",", "")}${r"#{"}${field.javaName}${r"}"}</#list>)
	</insert>
	
</mapper>