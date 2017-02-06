package com.infy.eng.messaging.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


public class MessageHelper 
{
	public final static Logger logger = Logger.getLogger(MessageHelper.class);

	/**
	 * Method to load the properties from properties file
	 * @return properties
	 */
	public static Properties loadProperties(String relativePath) 
	{
		Properties properties = new Properties();
		InputStream inputStream = null;
		String configPath = new File(".").getAbsolutePath();
		try 
		{
			
		}

		catch (Exception e)
		{
			logger.error("Error while loading the properties. Exception: " + e);
			throw new MessageException("config.not.loaded", "Unable to load the config entries", 2);
		}

		return (properties);
	}
}