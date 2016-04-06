package com.yyq.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yyq.core.Field;
import com.yyq.core.Model;
import com.yyq.core.builder.xml.ConfigParser;
import com.yyq.core.builder.xml.GeneratorPath;
import com.yyq.core.builder.xml.JdbcConfig;
import com.yyq.core.engine.FreeMarkerHelper;
import com.yyq.core.mapping.TypeMap;

public class Program {
	
	public static void main(String[] args) throws SQLException, FileNotFoundException {
		
		ClassLoader loader = Program.class.getClassLoader();
		
		generate(loader.getResource("").getPath() + "/generatorConfig.xml");
	}
	
	public static void generate(String generatorConfig) throws SQLException, FileNotFoundException {
		
		ConfigParser configParser = new ConfigParser();
		
		configParser.parse(new FileInputStream(generatorConfig));
		
		// 获取数据库连接
		Connection connection = getConnection(configParser.getJdbcConfig());
		// 获取表
		List<String> tables = getTables(connection);
		List<String> excludeTable = configParser.getExcludeTable();
		// 查询表结构
		Statement statement = connection.createStatement();
		
		TypeMap typeMap = configParser.getTypeMap();
		
		GeneratorPath javaModel = configParser.getJavaModelGenerator();
		GeneratorPath dao = configParser.getDaoGenerator();
		GeneratorPath daoImpl = configParser.getDaoImplGenerator();
		GeneratorPath service = configParser.getServiceGenerator();
		GeneratorPath serviceImpl = configParser.getServiceImplGenerator();
		GeneratorPath sqlMap = configParser.getSqlMapGenerator();
		GeneratorPath spring = configParser.getSpringGenerator();
		
		// 设置模板数据
		Map<String, Object> dataModel = new HashMap<String, Object>();
		dataModel.put("entityPackage", javaModel.getTargetPackage());
		dataModel.put("daoPackage", dao.getTargetPackage());
		dataModel.put("daoImplPackage", daoImpl.getTargetPackage());
		dataModel.put("servicePackage", service.getTargetPackage());
		dataModel.put("serviceImplPackage", serviceImpl.getTargetPackage());
		
		List<Model> models = new ArrayList<Model>();
		for (String tableName : tables) {
			if (!excludeTable.contains(tableName)) {
				Model model = new Model(tableName, configParser.tableNameConvert(tableName));
				
				String key = getPrimaryKey(connection, tableName);
				
				String sql = String.format("select column_name, data_type, column_comment from information_schema.columns a " +
						"where table_schema = '%s' and table_name = '%s'", connection.getCatalog(), tableName);
				
				ResultSet resultSet = statement.executeQuery(sql);
				
				Set<String> imports = new HashSet<String>();
				
				List<Field> fields = new ArrayList<Field>();
				while (resultSet.next()) {
					String column_comment = resultSet.getString("column_comment");
					String column_name = resultSet.getString("column_name");
					String data_type = resultSet.getString("data_type");
					
					Field field = new Field();
					field.setJdbcName(column_name);
					field.setJdbcType(data_type);
					field.setComment(column_comment);
					
					field.setJavaName(configParser.colunmNameConvert(column_name));
					field.setJavaType(typeMap.get(data_type));
					
					if (column_name.equals(key)) {
						model.setKey(field);
					} else {
						fields.add(field);
					}
				}
				model.setFields(fields);
				
				models.add(model);
				// 设置模板数据
				dataModel.put("model", model);
				dataModel.put("imports", imports);
				
				// 生成JavaModel
				FreeMarkerHelper.generate(String.format("%s/%s/%s.java", javaModel.getTargetProject(),
						javaModel.getTargetPackage().replaceAll("\\.", "/"), model.getClassName()), "template/entity.ftl", dataModel);
				// 生成Mapping
				FreeMarkerHelper.generate(String.format("%s/%s/%s.xml", sqlMap.getTargetProject(),
						sqlMap.getTargetPackage().replaceAll("\\.", "/"), model.getClassName()), "template/mapper.ftl", dataModel);
				
				// 生成Dao
				FreeMarkerHelper.generate(String.format("%s/%s/%sDAO.java", dao.getTargetProject(),
						dao.getTargetPackage().replaceAll("\\.", "/"), model.getClassName()), "template/dao.ftl", dataModel);
				
				// 生成DaoImpl
				FreeMarkerHelper.generate(String.format("%s/%s/%sDAOImpl.java", daoImpl.getTargetProject(),
						daoImpl.getTargetPackage().replaceAll("\\.", "/"), model.getClassName()), "template/daoImpl.ftl", dataModel);
				
				// 生成Service
				FreeMarkerHelper.generate(String.format("%s/%s/%sService.java", service.getTargetProject(),
						service.getTargetPackage().replaceAll("\\.", "/"), model.getClassName()), "template/service.ftl", dataModel);
				
				// 生成ServiceImpl
				FreeMarkerHelper.generate(String.format("%s/%s/%sServiceImpl.java", serviceImpl.getTargetProject(),
						serviceImpl.getTargetPackage().replaceAll("\\.", "/"), model.getClassName()), "template/serviceImpl.ftl", dataModel);
			}
		}
		
		// 设置模板数据
		dataModel.put("models", models);
		// 生成spring 配置
		FreeMarkerHelper.generate(String.format("%s/%s", spring.getTargetProject(),
				spring.getTargetPackage()), "template/spring.ftl", dataModel);
		
		
		// 释放查询资源 & 关闭数据库连接
		statement.close();
		connection.close();
		
		System.out.println("# 执行完成...");
	}
	
	public static Connection getConnection(JdbcConfig jdbcConfig) throws SQLException {
		/*
		URL url = new URL(jdbcConfig.getClassPath());
		
		URLClassLoader classLoader = new URLClassLoader(new URL[] {url}, Thread.currentThread().getContextClassLoader());
		
		Class.forName(jdbcConfig.getDriverClass(), true, classLoader);
		*/
		return DriverManager.getConnection(jdbcConfig.getConnectionURL(), jdbcConfig.getUserId(), jdbcConfig.getPassword());
	}
	
	public static List<String> getTables(Connection connection) {
		List<String> tables = null;
		try {
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet resultSet = databaseMetaData.getTables(connection.getCatalog(), "root", null, new String[] { "TABLE" });
			tables = new ArrayList<String>();
			while (resultSet.next()) {
				tables.add(resultSet.getString("TABLE_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tables;
	}
	
	public static String getPrimaryKey(Connection connection, String tableName) {
		try {
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet resultSet = databaseMetaData.getPrimaryKeys(connection.getCatalog(), null, tableName);
			while (resultSet.next()) {
				return resultSet.getString("COLUMN_NAME");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
