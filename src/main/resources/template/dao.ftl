package ${daoPackage};

import ${entityPackage}.${model.className};

/**
 * ${model.className}DAO
 * @author YYQ Model-Generator
 *
 */
public interface ${model.className}DAO {
	
	public ${model.className} get${model.className}ById(int ${firstToLower(model.className)}Id);
	
	public int delete${model.className}ById(int ${firstToLower(model.className)}Id);
	
	public int insert${model.className}(${model.className} ${firstToLower(model.className)});
	
	public int updateBy${model.className}(${model.className} ${firstToLower(model.className)});
}