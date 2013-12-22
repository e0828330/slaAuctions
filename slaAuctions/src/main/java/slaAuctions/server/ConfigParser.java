package slaAuctions.server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import slaAuctions.entities.Template;

public class ConfigParser {

	/* Stores the results as type : template list */
	private Map<String, ArrayList<Template>> provider = new HashMap<String, ArrayList<Template>>();
	private Map<String, ArrayList<Template>> customer = new HashMap<String, ArrayList<Template>>();

	/* Used internally for building the templates */
	private ArrayList<Double> list = new ArrayList<Double>(2);
	private Map<String, Integer> minValues = new HashMap<String, Integer>();
	private Map<String, Integer> currentValues = new HashMap<String, Integer>();
	private Map<String, Integer> maxValues = new HashMap<String, Integer>();

	/* Used for the normal distribution of values */
	private NormalDistribution nd;

	/* Unique agent id */
	private int id;
	
	/* The ini file */
	private Ini ini;
	
	/* Stores the maximum number of properties */
	private int maxProperies;

	private ArrayList<String> properties = new ArrayList<String>();
	
	public void doParse() throws InvalidFileFormatException, IOException {
		ini = new Ini(new File("src/main/resources/test.ini"));
		provider.put("english", new ArrayList<Template>());
		customer.put("english", new ArrayList<Template>());

		maxProperies = 0;
		for (String key : ini.keySet()) {
			if (key.equals("Provider") || key.equals("Customer") || key.equals("Price")) {
				continue;
			}
			properties.add(key);
			maxProperies++;
		}
		
		// TODO: Validate must at least have > 3 properties and a price
		parseEnglishProviders();
		parseEnglishCustomers();
	}
	
	private Template createTemplate(Integer providerId) {
		Template tpl = new Template();
		tpl.setProviderId(providerId);
		
		int i = 0;
		
		try {
			for (String key : properties) {
				if (key.equals("Price")) {
					PropertyUtils.setSimpleProperty(tpl, "price_min", minValues.get("Price"));
					PropertyUtils.setSimpleProperty(tpl, "price", currentValues.get("Price"));
					PropertyUtils.setSimpleProperty(tpl, "price_max", minValues.get("Price"));
				}
				else {
					PropertyUtils.setSimpleProperty(tpl, "property" + i + "_min", minValues.get(key));
					PropertyUtils.setSimpleProperty(tpl, "property" + i + "_current", currentValues.get(key));
					PropertyUtils.setSimpleProperty(tpl, "property" + i + "_max", minValues.get(key));
				}
				i++;
			}
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return tpl;
	}

	private void parseEnglishProviders() {
		for (int i = 0; i < Integer.parseInt(ini.get("Provider", "english")); i++) {
			System.out.println("Provider nr." + (id++) + " - type english");

			minValues.clear();
			maxValues.clear();
			currentValues.clear();

			int propertyCount = getPropertyCount(3, maxProperies);
			
			System.out.println("Number of properties: " + propertyCount);
			
			for (String key : properties) {
				nd = new NormalDistribution(Double.parseDouble(ini.get(key, "mean")), Double.parseDouble(ini.get(key, "sd")));

				/* For providers only the price is variable */
				if (!key.equals("Price")) {
					if (propertyCount > 0) {
						System.out.println(key + " = " + ((int) nd.sample()));
						currentValues.put(key, ((int) nd.sample()));
					}
					propertyCount--;
				} else {
					list.clear();
					list.add(nd.sample());
					list.add(nd.sample());
					Collections.sort(list);
					minValues.put(key, list.get(0).intValue());
					maxValues.put(key, list.get(1).intValue());
					/* Starts of with the maximum */
					currentValues.put(key, list.get(1).intValue());

					System.out.println(key + " - min = " + list.get(0).intValue());
					System.out.println(key + " - max = " + list.get(1).intValue());
				}
			}

			provider.get("english").add(createTemplate(id - 1));
			System.out.println("----");
		}
	}
	
	private void parseEnglishCustomers() {
		for (int i = 0; i < Integer.parseInt(ini.get("Customer", "english")); i++) {
			System.out.println("Customer nr." + (id++) + " - type english");

			minValues.clear();
			maxValues.clear();
			currentValues.clear();
			
			int propertyCount = getPropertyCount(3, maxProperies);
			
			System.out.println("Number of properties: " + propertyCount);

			for (String key : properties) {
	
				nd = new NormalDistribution(Double.parseDouble(ini.get(key, "mean")), Double.parseDouble(ini.get(key, "sd")));
				
				if (propertyCount > 0 || key.equals("Price")) {
					list.clear();
					list.add(nd.sample());
					list.add(nd.sample());
					Collections.sort(list);
					minValues.put(key, list.get(0).intValue());
					maxValues.put(key, list.get(1).intValue());
	
					System.out.println(key + " - min = " + list.get(0).intValue());
					System.out.println(key + " - max = " + list.get(1).intValue());
				}
				
				if (!key.equals("Price")) {
					propertyCount--;
				}
			}

			customer.get("english").add(createTemplate(null));
			System.out.println("----");
		}
	}
	
	private Integer getPropertyCount(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

	public Map<String, ArrayList<Template>> getProvider() {
		return provider;
	}

	public Map<String, ArrayList<Template>> getCustomer() {
		return customer;
	}

	public static void main(String[] args) throws InvalidFileFormatException, IOException {
		ConfigParser parser = new ConfigParser();
		parser.doParse();
	}
}
