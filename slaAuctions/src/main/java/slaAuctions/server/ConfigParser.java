package slaAuctions.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

public class ConfigParser {
	public static void main(String[] args) throws InvalidFileFormatException, IOException {
		Ini ini = new Ini(new File("src/main/resources/test.ini"));
		int n = 1;
		int i;
		NormalDistribution nd;
		// Providers - english
		for (i = 0; i < Integer.parseInt(ini.get("Provider", "english")); i++) {
			System.out.println("Provider nr." + (n++) + " - type english");
			for (String key : ini.keySet()) {
				if (key.equals("Provider") || key.equals("Customer")) {
					continue;
				}

				nd = new NormalDistribution(Double.parseDouble(ini.get(key, "mean")), Double.parseDouble(ini.get(key, "sd")));
				
				if (!key.equals("Price")) {
					System.out.println(key + "= " + ((int)nd.sample()));
				}
				else {
					ArrayList<Double> list = new ArrayList<Double>(3);
					list.add(nd.sample());
					list.add(nd.sample());
					list.add(nd.sample());
					Collections.sort(list);
					System.out.println(key + " - min = " + list.get(0).intValue());
					System.out.println(key + " - val = " + list.get(1).intValue());
					System.out.println(key + " - max = " + list.get(2).intValue());
				}
			}
			System.out.println("----");
		}
	}
}
