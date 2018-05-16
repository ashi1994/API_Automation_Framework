package com.core;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class URL {

	private static Logger log = LogManager.getLogger(URL.class.getName());
	public static final String URL = "https://reqres.in";
	
	public static String getEndPoint(){
		log.info("Base URI : " + URL);
		return URL;
	}
	
	public static String getEndPoint(String resource){
		log.info("URI End Point : " + URL + resource);
		return URL + resource;
	}	
}