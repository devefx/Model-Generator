package com.yyq.core.mapping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Convert {
	
	private Pattern pattern;
	
	private String replacement;
	
	public Convert(String regex, String replacement) {
		this.pattern = Pattern.compile(regex);
		this.replacement = replacement;
	}
	
	public String format(String input) {
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return process(matcher.replaceAll(replacement));
		}
		return null;
	}
	
	private String process(String input) {
		Pattern pattern = Pattern.compile("_(.)");
		Matcher matcher = pattern.matcher(input);
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(buffer, matcher.group(1).toUpperCase());
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}
}
