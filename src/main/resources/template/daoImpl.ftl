package ${daoImplPackage};

import org.mybatis.spring.support.SqlSessionDaoSupport;

import ${daoPackage}.${model.className}DAO;
import ${entityPackage}.${model.className};

/**
 * ${model.className}DAOImpl
 * @author YYQ Model-Generator
 *
 */
public class ${model.className}DAOImpl extends SqlSessionDaoSupport implements ${model.className}DAO {

	@Override
	public ${model.className} get${model.className}ById(int ${firstToLower(model.className)}Id) {
		return getSqlSession().selectOne("get${model.className}ById", ${firstToLower(model.className)}Id);
	}

	@Override
	public int delete${model.className}ById(int ${firstToLower(model.className)}Id) {
		return getSqlSession().delete("delete${model.className}ById", ${firstToLower(model.className)}Id);
	}

	@Override
	public int insert${model.className}(${model.className} ${firstToLower(model.className)}) {
		return getSqlSession().insert("insert${model.className}", ${firstToLower(model.className)});
	}

	@Override
	public int updateBy${model.className}(${model.className} ${firstToLower(model.className)}) {
		return getSqlSession().update("updateBy${model.className}", ${firstToLower(model.className)});
	}

}