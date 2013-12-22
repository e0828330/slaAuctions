package slaAuctions.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import slaAuctions.agents.RevEnglishCustomer;
import slaAuctions.agents.RevEnglishProvider;
import slaAuctions.entities.Template;
import slaAuctions.providerBeans.ServerBean;
import slaAuctions.utils.DataGridConnectionUtility;

public class Main {

	public static void main(String[] args) throws Exception {
		/* Create the space if it does not exist */
		DataGridConnectionUtility.getSpace("auctionSpace");
		/* Create app context */
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("file:src/main/resources/Application.xml");
		
		ServerBean bean = (ServerBean) context.getBean("serverBean");

		/* Wait for match notifications */
		bean.doNotify();
		
		ExecutorService executor = Executors.newCachedThreadPool();
		
		/* Hardcoded for test */
	/*	Map<String, Integer> minValues = new HashMap<String, Integer>();
		Map<String, Integer> currentValues = new HashMap<String, Integer>();
		Map<String, Integer> maxValues = new HashMap<String, Integer>();
		
		minValues.put("Price", 100);
		currentValues.put("Price", 600);
		maxValues.put("Price", 600);
		currentValues.put("Cores", 2);
		
		Template providerTpl = new Template(minValues, currentValues, maxValues);
		providerTpl.setProperty0(90);
		providerTpl.setProviderId(1);
		
		RevEnglishProvider provider = new RevEnglishProvider(context, providerTpl);
		executor.execute(provider);
		
		minValues.clear();
		maxValues.clear();
		currentValues.clear();

		minValues.put("Price", 100);
		currentValues.put("Price", 300);
		maxValues.put("Price", 300);
		currentValues.put("Cores", 2);
		
		Template providerTpl2 = new Template(minValues, currentValues, maxValues);
		providerTpl2.setProviderId(2);
		
		RevEnglishProvider provider2 = new RevEnglishProvider(context, providerTpl2);
		executor.execute(provider2);
		
		minValues.clear();
		maxValues.clear();
		currentValues.clear();
		
		minValues.put("Price", 0);
		maxValues.put("Price", 120);
		minValues.put("Cores", 4);
		maxValues.put("Cores", 10);
		
		Template customerTpl = new Template(minValues, currentValues, maxValues);
		providerTpl.setProperty0(100);
		RevEnglishCustomer customer = new RevEnglishCustomer(context, customerTpl);
		executor.execute(customer);

		minValues.clear();
		maxValues.clear();
		currentValues.clear();
		
		/*
		minValues.put("Price", 0);
		maxValues.put("Price", 200);
		minValues.put("Cores", 3);
		maxValues.put("Cores", 9);
		
		Template customerTpl2 = new Template(minValues, currentValues, maxValues);
		RevEnglishCustomer customer2 = new RevEnglishCustomer(context, customerTpl2);
		executor.execute(customer2);

		minValues.clear();
		maxValues.clear();
		currentValues.clear();*/
		
		
		ConfigParser parser = new ConfigParser();
		parser.doParse();
		/*for (Template t : parser.getCustomer().get("english")) {
			RevEnglishCustomer c = new RevEnglishCustomer(context, t);
			executor.execute(c);
		}*/
		for (Template t : parser.getProvider().get("english")) {
			RevEnglishProvider p = new RevEnglishProvider(context, t);
			executor.execute(p);
		}
		
		
	//	executor.awaitTermination(2, TimeUnit.MINUTES);
		
	}

}
