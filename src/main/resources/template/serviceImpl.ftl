package ${serviceImplPackage};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${daoPackage}.${model.className}DAO;
import ${entityPackage}.${model.className};
import ${servicePackage}.${model.className}Service;

/**
 * ${model.className}ServiceImpl
 * @author YYQ Model-Generator
 *
 */
@Service
public class ${model.className}ServiceImpl implements ${model.className}Service {
	
	@Autowired
	private ${model.className}DAO ${firstToLower(model.className)}DAO;
	
	@Override
	public ${model.className} get${model.className}ById(int ${firstToLower(model.className)}Id) {
		return ${firstToLower(model.className)}DAO.get${model.className}ById(${firstToLower(model.className)}Id);
	}

	@Override
	public int delete${model.className}ById(int ${firstToLower(model.className)}Id) {
		return ${firstToLower(model.className)}DAO.delete${model.className}ById(${firstToLower(model.className)}Id);
	}

	@Override
	public int insert${model.className}(${model.className} ${firstToLower(model.className)}) {
		return ${firstToLower(model.className)}DAO.insert${model.className}(${firstToLower(model.className)});
	}

	@Override
	public int updateBy${model.className}(${model.className} ${firstToLower(model.className)}) {
		return ${firstToLower(model.className)}DAO.updateBy${model.className}(${firstToLower(model.className)});
	}

}
