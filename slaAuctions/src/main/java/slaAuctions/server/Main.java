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
import slaAuctions.utils.DataGridConnectionUtility;

public class Main {

	public static void main(String[] args) throws Exception {
		/* Create the space if it does not exist */
		DataGridConnectionUtility.getSpace("auctionSpace");
		/* Create app context */
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("file:src/main/resources/Application.xml");
		
		/*ServerBean bean = (ServerBean) context.getBean("serverBean");

		bean.write();
		bean.doNotify();*/
		
		ExecutorService executor = Executors.newCachedThreadPool();
		
		/* Hardcoded for test */
		Map<String, Integer> minValues = new HashMap<String, Integer>();
		Map<String, Integer> currentValues = new HashMap<String, Integer>();
		Map<String, Integer> maxValues = new HashMap<String, Integer>();
		
		/* Provider 1 */
		minValues.put("Price", 100);
		currentValues.put("Price", 600);
		maxValues.put("Price", 600);
		currentValues.put("Cores", 2);
		
		Template providerTpl = new Template(minValues, currentValues, maxValues);
		providerTpl.setProviderId(1);
		
		RevEnglishProvider provider = new RevEnglishProvider(context, providerTpl);
		executor.execute(provider);
		
		minValues.clear();
		maxValues.clear();
		currentValues.clear();

		/* Provider 2 */
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
		
		/* Customer 1 */
		
		minValues.put("Price", 0);
		maxValues.put("Price", 120);
		minValues.put("Cores", 1);
		maxValues.put("Cores", 4);
		
		Template customerTpl = new Template(minValues, currentValues, maxValues);
		RevEnglishCustomer customer = new RevEnglishCustomer(context, customerTpl);
		executor.execute(customer);

		minValues.clear();
		maxValues.clear();
		currentValues.clear();
		
		/* Customer 2 */
		
		minValues.put("Price", 0);
		maxValues.put("Price", 200);
		minValues.put("Cores", 1);
		maxValues.put("Cores", 3);
		
		Template customerTpl2 = new Template(minValues, currentValues, maxValues);
		RevEnglishCustomer customer2 = new RevEnglishCustomer(context, customerTpl2);
		executor.execute(customer2);

		minValues.clear();
		maxValues.clear();
		currentValues.clear();
		
		executor.awaitTermination(2, TimeUnit.MINUTES);
		
	}

}
