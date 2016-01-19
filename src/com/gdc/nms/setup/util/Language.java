package com.gdc.nms.setup.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Language {
	private static ResourceBundle BUNDLE;
	
	public static void load() {
		BUNDLE = ResourceBundle.getBundle("com.gdc.nms.setup.language");
	}

	public static void load(Locale locale) throws MissingResourceException {
		BUNDLE = ResourceBundle.getBundle("com.gdc.nms.setup.language", locale);
	}

	public static String get(String key) {
		return BUNDLE.getString(key);
	}

	public static String getTitle() {
		return BUNDLE.getString("window.title");
	}

}
