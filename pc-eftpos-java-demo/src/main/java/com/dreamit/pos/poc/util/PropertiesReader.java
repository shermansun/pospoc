package com.dreamit.pos.poc.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ssun on 29/10/17.
 */
public class PropertiesReader {

	private static Properties properties;

	public static java.util.Properties loadProperties() {

		FileInputStream fi = null;
		InputStream is = null;
		try {
			is = PropertiesReader.class.getResourceAsStream("/com/printer/printer-dispatcher.properties");
			properties.load(is);
		} catch (Exception ex) {
			//logger.error(ex.getMessage(), ex);
		} finally {
			try {
				if (fi != null) {
					fi.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException ex) {
				//logger.error(ex.getMessage(), ex);
			}
		}

		return properties;
	}

	public static Properties getProperties() {
		return properties;
	}

	public static void setProperties(Properties properties) {
		PropertiesReader.properties = properties;
	}

	public static void myFirstFunctiobn(String str) {
		// There is some difficult
	}
}
