package com.common;


import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.common.EasyBrokEntityManagerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.w3c.dom.Element;
import org.w3c.dom.CharacterData;

@Controller
@RequestMapping("/vrp")
public class EasyBrokController {
	private static Logger logger = Logger.getLogger(EasyBrokController.class);
	private static Logger syncLogger = Logger.getLogger("com.common");
	
	
	@RequestMapping(value = "/getRoutes1", method = RequestMethod.GET)
	public @ResponseBody String generateVRPOld(ModelMap model, HttpServletRequest request)
	{
		logger.debug("EasyBrokController.VRP");
		String input_directory = request.getParameter("input");
		String output_directory = request.getParameter("output");
		String config_directory = request.getParameter("config");
		System.out.println("Input - " + input_directory);
		System.out.println("Output - " + output_directory);
		System.out.println("Config - " + config_directory);
		/*
		BicycleMessenger1 obj = new BicycleMessenger1(input_directory, output_directory, config_directory);
		try{
		obj.generatePaths();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		*/
		String response = "Execution Successful<br>Please find detailed output at -> " + output_directory + "/detailed_output.fmt";
		return response;
	}
	
	@RequestMapping(value = "/getRoutes", method = RequestMethod.POST)
	public @ResponseBody String generateVRP(ModelMap model, HttpServletRequest request)
	{
		logger.debug("EasyBrokController.VRP");
		String basePath;
		URL resource = getClass().getResource("/");
		String path = resource.getPath();
		basePath = path;
		//basePath = E:/Invero/apache-tomcat-6.0.43/webapps/jsprit-web-1.5.1-SNAPSHOT/WEB-INF/classes/../../input"basePath.replaceFirst("/", "");
		String input_directory = basePath + "../../input";
		String output_directory = basePath + "../../output";
		String config_directory = basePath + "../../input";
		String messengerXML = request.getParameter("messenger");
		String envelopXML = request.getParameter("envelop");
		System.out.println("Input - " + input_directory);
		System.out.println("Output - " + output_directory);
		System.out.println("Config - " + config_directory);
		System.out.println("Messenger - " + messengerXML);
		System.out.println("Envelop - " + envelopXML);
		
		BicycleMessenger1 obj = new BicycleMessenger1(input_directory, output_directory, config_directory, messengerXML, envelopXML);
		try{
		obj.generatePaths();
		BufferedReader reader = new BufferedReader(new FileReader(output_directory + "/bicycleMessenger.xml"));
		String response = "", line;
		//String response = "Execution Successful<br>Please find detailed output at -> " + output_directory + "/detailed_output.fmt";
		while((line = reader.readLine()) != null)
			response = response + line + "\n";
		return response;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/saveWebContent", method = RequestMethod.POST)
	public @ResponseBody String saveWebContent(ModelMap model, HttpServletRequest request)
	{
		try{
		//String path = "E:\\Invero\\apache-tomcat-6.0.43\\webapps\\PineappleEasyRealState\\output";
		String path = "/var/lib/tomcat6/webapps/PineappleEasyRealState/output";
		
		System.out.println("Hiiiiii");
		logger.debug("EasyBrokController.VRP");
		String content = request.getParameter("data");
		
		String url = request.getParameter("url");
		
		String random_file_name = "data_" + (EasyBrokEntityManagerFactory.r.nextDouble() + "").replace('.', '_') + ".dat";
		//System.out.println(content);
		System.out.println(random_file_name);
		BufferedWriter fwriter = new BufferedWriter(new FileWriter(path + "/" + random_file_name));
		fwriter.write(content);
		fwriter.close();
		WebHistory.addToIndex(url, content, random_file_name);
		return "Success";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "Fail";
		
		}
		
		
		
	}
	
	@RequestMapping(value = "/getSearchResults", method = RequestMethod.GET)
	public @ResponseBody String getSearchResults(ModelMap model, HttpServletRequest request)
	{
		try{
		String keyword = request.getParameter("keyword");
		ArrayList<String[]> results = EasyBrokEntityManagerFactory.webHistory.getTopResults(keyword, 50);
		Gson gson = new GsonBuilder().create();
        String response = gson.toJson(results);
		return response;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "Fail";
		
		}
		
		
		
	}
	
	 
	}
