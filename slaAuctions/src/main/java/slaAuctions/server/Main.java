package slaAuctions.server;

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
		
		
		ConfigParser parser = new ConfigParser();
		parser.doParse();
		for (Template t : parser.getCustomer().get("english")) {
			RevEnglishCustomer c = new RevEnglishCustomer(context, t);
			executor.execute(c);
		}
		for (Template t : parser.getProvider().get("english")) {
			RevEnglishProvider p = new RevEnglishProvider(context, t);
			executor.execute(p);
		}
		
		executor.awaitTermination(2, TimeUnit.MINUTES);
		System.out.println("Total matches: " + bean.getMatches());
		System.exit(0);
	}

}
