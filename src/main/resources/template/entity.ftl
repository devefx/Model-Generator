package ${entityPackage};

<#if imports?exists && imports?size != 0>
<#list imports as import>
import ${import};
</#list>

</#if>
/**
 * ${model.className}
 * @author YYQ Model-Generator
 *
 */
public class ${model.className} {

	<#if model.key??>
	private ${model.key.javaType} ${model.key.javaName};
	</#if>
	<#if model.fields?exists>
	<#list model.fields as field>
	private ${field.javaType} ${field.javaName};
	</#list>
	
	<#if model.key??>
	/**
	 * 获取${model.key.comment}
	 * @return
	 */
	public ${model.key.javaType} get${firstToUpper(model.key.javaName)}() {
		return ${model.key.javaName};
	}
	/**
	 * 设置${model.key.comment}
	 * @param ${model.key.javaName}
	 */
	public void set${firstToUpper(model.key.javaName)}(${model.key.javaType} ${model.key.javaName}) {
		this.${model.key.javaName} = ${model.key.javaName};
	}
	</#if>
	</#if>
	<#list model.fields as field>
	/**
	 * 获取${field.comment}
	 * @return
	 */
	public ${field.javaType} get${firstToUpper(field.javaName)}() {
		return ${field.javaName};
	}
	/**
	 * 设置${field.comment}
	 * @param ${field.javaName}
	 */
	public void set${firstToUpper(field.javaName)}(${field.javaType} ${field.javaName}) {
		this.${field.javaName} = ${field.javaName};
	}
	</#list>
}