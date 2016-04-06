package com.yyq.core.builder.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.yyq.core.mapping.Convert;
import com.yyq.core.mapping.SimpleTypeMap;
import com.yyq.core.mapping.TypeMap;

public class ConfigParser {
	
	private JdbcConfig jdbcConfig;
	
	private List<String> excludeTable;
	
	private TypeMap typeMap;
	
	private List<Convert> tableNameConvert;
	
	private List<Convert> colunmNameConvert;
	
	private GeneratorPath javaModelGenerator;
	
	private GeneratorPath daoGenerator;
	
	private GeneratorPath daoImplGenerator;
	
	private GeneratorPath serviceGenerator;
	
	private GeneratorPath serviceImplGenerator;
	
	private GeneratorPath sqlMapGenerator;
	
	private GeneratorPath springGenerator;
	
	public ConfigParser() {
		jdbcConfig = new JdbcConfig();
		excludeTable = new ArrayList<String>();
		typeMap = new SimpleTypeMap();
		tableNameConvert = new ArrayList<Convert>();
		colunmNameConvert = new ArrayList<Convert>();
	}
	
	public void parse(InputStream inputStream) {
		try {
			SAXReader saxReader = new SAXReader();
			saxReader.setValidation(false);
			parseDocument(saxReader.read(inputStream));
		} catch (Exception e) {
			throw new RuntimeException("Error occurred.  Cause: " + e, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void parseDocument(Document document) throws Exception {
		// JDBC Config
		Node classPath = document.selectSingleNode("/generatorConfiguration/jdbcConnection/classPath");
		Node driverClass = document.selectSingleNode("/generatorConfiguration/jdbcConnection/driverClass");
		Node connectionURL = document.selectSingleNode("/generatorConfiguration/jdbcConnection/connectionURL");
		Node userId = document.selectSingleNode("/generatorConfiguration/jdbcConnection/userId");
		Node password = document.selectSingleNode("/generatorConfiguration/jdbcConnection/password");
		
		jdbcConfig.setClassPath(classPath.getText());
		jdbcConfig.setDriverClass(driverClass.getText());
		jdbcConfig.setConnectionURL(connectionURL.getText());
		jdbcConfig.setUserId(userId.getText());
		jdbcConfig.setPassword(password.getText());
		
		// javaModelGenerator
		Node model = document.selectSingleNode("/generatorConfiguration/javaModelGenerator");
		javaModelGenerator = new GeneratorPath(model.valueOf("@targetPackage"), model.valueOf("@targetProject"));
		
		// daoGenerator
		Node dao = document.selectSingleNode("/generatorConfiguration/daoGenerator");
		daoGenerator = new GeneratorPath(dao.valueOf("@targetPackage"), dao.valueOf("@targetProject"));
		
		// daoImplGenerator
		Node daoImpl = document.selectSingleNode("/generatorConfiguration/daoImplGenerator");
		daoImplGenerator = new GeneratorPath(daoImpl.valueOf("@targetPackage"), daoImpl.valueOf("@targetProject"));
		
		// serviceGenerator
		Node service = document.selectSingleNode("/generatorConfiguration/serviceGenerator");
		serviceGenerator = new GeneratorPath(service.valueOf("@targetPackage"), service.valueOf("@targetProject"));
		
		// serviceImplGenerator
		Node serviceImp = document.selectSingleNode("/generatorConfiguration/serviceImplGenerator");
		serviceImplGenerator = new GeneratorPath(serviceImp.valueOf("@targetPackage"), serviceImp.valueOf("@targetProject"));
		
		// sqlMapGenerator
		Node sqlMap = document.selectSingleNode("/generatorConfiguration/sqlMapGenerator");
		sqlMapGenerator = new GeneratorPath(sqlMap.valueOf("@targetPackage"), sqlMap.valueOf("@targetProject"));
		
		// springGenerator
		Node spring = document.selectSingleNode("/generatorConfiguration/springGenerator");
		springGenerator = new GeneratorPath(spring.valueOf("@targetFile"), spring.valueOf("@targetProject"));
		
		// excludeTable
		List<Node> tables = document.selectNodes("/generatorConfiguration/excludeTable/table");
		for (Node node : tables) {
			excludeTable.add(node.valueOf("@tableName"));
		}
		
		// typeMapping
		String type = document.valueOf("/generatorConfiguration/typeMapping/@default");
		if ("simple".equals(type)) {
			typeMap = new SimpleTypeMap();
		} else if ("".equals(type)) {
			typeMap = new SimpleTypeMap();
		} else {
			typeMap = (TypeMap) Class.forName(type).newInstance();
		}
		List<Node> types = document.selectNodes("/generatorConfiguration/typeMapping/type");
		for (Node node : types) {
			typeMap.set(node.valueOf("@jdbcName"), node.valueOf("@javaName"));
		}
		
		// tableNameConvert
		List<Node> formats = document.selectNodes("/generatorConfiguration/tableNameConvert/format");
		for (Node node : formats) {
			tableNameConvert.add(new Convert(node.valueOf("@regex"), node.valueOf("@replacement")));
		}
		
		// colunmNameConvert
		formats = document.selectNodes("/generatorConfiguration/colunmNameConvert/format");
		for (Node node : formats) {
			colunmNameConvert.add(new Convert(node.valueOf("@regex"), node.valueOf("@replacement")));
		}
	}

	public JdbcConfig getJdbcConfig() {
		return jdbcConfig;
	}

	public List<String> getExcludeTable() {
		return excludeTable;
	}

	public TypeMap getTypeMap() {
		return typeMap;
	}
	
	public String tableNameConvert(String name) {
		for (Convert convert : tableNameConvert) {
			String result = convert.format(name);
			if (result != null) {
				return result;
			}
		}
		Convert convert = new Convert("(.+)", "_$1");
		return convert.format(name);
	}
	
	public String colunmNameConvert(String name) {
		for (Convert convert : colunmNameConvert) {
			String result = convert.format(name);
			if (result != null) {
				return result;
			}
		}
		Convert convert = new Convert("", "");
		return convert.format(name);
	}

	public GeneratorPath getJavaModelGenerator() {
		return javaModelGenerator;
	}

	public GeneratorPath getDaoGenerator() {
		return daoGenerator;
	}

	public GeneratorPath getDaoImplGenerator() {
		return daoImplGenerator;
	}
	
	public GeneratorPath getServiceGenerator() {
		return serviceGenerator;
	}

	public GeneratorPath getServiceImplGenerator() {
		return serviceImplGenerator;
	}

	public GeneratorPath getSqlMapGenerator() {
		return sqlMapGenerator;
	}

	public GeneratorPath getSpringGenerator() {
		return springGenerator;
	}
}

