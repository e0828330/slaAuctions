package slaAuctions.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import slaAuctions.entities.Template;

public class ConfigParser {

	private Map<String, ArrayList<Template>> provider = new HashMap<String, ArrayList<Template>>();
	private Map<String, ArrayList<Template>> customer = new HashMap<String, ArrayList<Template>>();

	private ArrayList<Double> list = new ArrayList<Double>(2);
	private Map<String, Integer> minValues = new HashMap<String, Integer>();
	private Map<String, Integer> currentValues = new HashMap<String, Integer>();
	private Map<String, Integer> maxValues = new HashMap<String, Integer>();

	private NormalDistribution nd;

	private int n;
	private Ini ini;

	public void doParse() throws InvalidFileFormatException, IOException {
		ini = new Ini(new File("src/main/resources/test.ini"));
		provider.put("english", new ArrayList<Template>());
		customer.put("english", new ArrayList<Template>());

		parseEnglishProviders();
		parseEnglishCustomers();
	}

	private void parseEnglishProviders() {
		for (int i = 0; i < Integer.parseInt(ini.get("Provider", "english")); i++) {
			System.out.println("Provider nr." + (n++) + " - type english");

			minValues.clear();
			maxValues.clear();
			currentValues.clear();

			for (String key : ini.keySet()) {
				if (key.equals("Provider") || key.equals("Customer")) {
					continue;
				}

				nd = new NormalDistribution(Double.parseDouble(ini.get(key, "mean")), Double.parseDouble(ini.get(key, "sd")));

				/* For providers only the price is variable */
				if (!key.equals("Price")) {
					System.out.println(key + " = " + ((int) nd.sample()));
					currentValues.put(key, ((int) nd.sample()));
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

			Template tpl = new Template(minValues, currentValues, maxValues);
			provider.get("english").add(tpl);
			System.out.println("----");
		}
	}

	private void parseEnglishCustomers() {
		for (int i = 0; i < Integer.parseInt(ini.get("Customer", "english")); i++) {
			System.out.println("Customer nr." + (n++) + " - type english");

			minValues.clear();
			maxValues.clear();
			currentValues.clear();

			for (String key : ini.keySet()) {
				if (key.equals("Provider") || key.equals("Customer")) {
					continue;
				}

				nd = new NormalDistribution(Double.parseDouble(ini.get(key, "mean")), Double.parseDouble(ini.get(key, "sd")));

				list.clear();
				list.add(nd.sample());
				list.add(nd.sample());
				Collections.sort(list);
				minValues.put(key, list.get(0).intValue());
				maxValues.put(key, list.get(1).intValue());

				System.out.println(key + " - min = " + list.get(0).intValue());
				System.out.println(key + " - max = " + list.get(1).intValue());
			}

			Template tpl = new Template(minValues, currentValues, maxValues);
			customer.get("english").add(tpl);
			System.out.println("----");
		}
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
