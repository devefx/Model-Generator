package com.yyq.core.engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.SimpleNumber;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
/**
 * FreeMarkerHelper
 * @author YYQ 	2015/7/22
 *
 */
public class FreeMarkerHelper {
	/**
	 * 生成jsp静态页面
	 * @param path
	 * @param filename
	 * @param dataModel
	 * @return
	 */
	public static boolean generate(String path, String ftlName, Map<String, Object> dataModel) {
		// 添加模板方法
		addTemplateMethod(dataModel);
		// 创建目录
		ClassLoader loader = FreeMarkerHelper.class.getClassLoader();
		String root = loader.getResource("").getPath();
		// 解析目录和文件名
		int index = path.lastIndexOf("/");
		String directory = path.substring(0, index);
		String filename = path.substring(index + 1);
		// 若目录不存在则创建目录
		File file = new File(directory);
		if (!file.exists()) {
			file.mkdirs();
		}
		Configuration configuration = new Configuration();
		configuration.setDefaultEncoding("UTF-8");
		try {
			configuration.setDirectoryForTemplateLoading(new File(root));
			OutputStream outputStream = new FileOutputStream(file.getPath() + File.separator + filename);
			Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
			Template template = configuration.getTemplate(ftlName, "UTF-8");
			template.process(dataModel, writer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (TemplateException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 设置静态模型
	 * @param dataModel
	 * @param clazz
	 */
	public static void setDefaultStaticModel(Map<String, Object> dataModel, Class<?> ...classes) {
		for (Class<?> clazz : classes) {
			BeansWrapper beansWrapper = BeansWrapper.getDefaultInstance();
			TemplateHashModel model = beansWrapper.getStaticModels();
			try {
				dataModel.put(clazz.getSimpleName(), model.get(clazz.getName()));
			} catch (TemplateModelException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static void addTemplateMethod(Map<String, Object> dataModel) {
		dataModel.put("encode", new TemplateMethodModelEx() {
			public Object exec(List args) throws TemplateModelException {
				if (args == null || args.size() < 2) {
					throw new TemplateModelException("Parameter is less than 2");
				}
				try {
					return URLEncoder.encode(args.get(0).toString(), args.get(1).toString());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		dataModel.put("decode", new TemplateMethodModelEx() {
			public Object exec(List args) throws TemplateModelException {
				if (args == null || args.size() < 2) {
					throw new TemplateModelException("Parameter is less than 2");
				}
				try {
					return URLDecoder.decode(args.get(0).toString(), args.get(1).toString());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		dataModel.put("min", new TemplateMethodModelEx() {
			public Object exec(List args) throws TemplateModelException {
				if (args == null || args.size() < 2) {
					throw new TemplateModelException("Parameter is less than 2");
				}
				Number value1 = ((SimpleNumber) args.get(0)).getAsNumber();
				Number value2 = ((SimpleNumber) args.get(1)).getAsNumber();
				if (value1 instanceof Integer && value2 instanceof Integer) {
					return Math.min(value1.intValue(), value2.intValue());
				}
				return Math.min(value1.doubleValue(), value2.doubleValue());
			}
		});
		dataModel.put("max", new TemplateMethodModelEx() {
			public Object exec(List args) throws TemplateModelException {
				if (args == null || args.size() < 2) {
					throw new TemplateModelException("Parameter is less than 2");
				}
				Number value1 = ((SimpleNumber) args.get(0)).getAsNumber();
				Number value2 = ((SimpleNumber) args.get(1)).getAsNumber();
				if (value1 instanceof Integer && value2 instanceof Integer) {
					return Math.max(value1.intValue(), value2.intValue());
				}
				return Math.max(value1.doubleValue(), value2.doubleValue());
			}
		});
		dataModel.put("firstToUpper", new TemplateMethodModelEx() {
			public Object exec(List args) throws TemplateModelException {
				if (args == null || args.size() < 1) {
					throw new TemplateModelException("Parameter is less than 1");
				}
				String text = args.get(0).toString();
				String first = text.substring(0, 1);
				return text.replaceFirst(first, first.toUpperCase());
			}
		});
		dataModel.put("firstToLower", new TemplateMethodModelEx() {
			public Object exec(List args) throws TemplateModelException {
				if (args == null || args.size() < 1) {
					throw new TemplateModelException("Parameter is less than 1");
				}
				String text = args.get(0).toString();
				String first = text.substring(0, 1);
				return text.replaceFirst(first, first.toLowerCase());
			}
		});
		dataModel.put("cut", new TemplateMethodModelEx() {
			public Object exec(List args) throws TemplateModelException {
				if (args == null || args.size() < 2) {
					throw new TemplateModelException("Parameter is less than 2");
				}
				if (args.get(0) != null) {
					String text = args.get(0).toString();
					int length = ((SimpleNumber)args.get(1)).getAsNumber().intValue();
					if (text.length() > length) {
						return text.substring(0, length - 1) + "...";
					}
					return text;
				}
				return "";
			}
		});
	}
	
}
