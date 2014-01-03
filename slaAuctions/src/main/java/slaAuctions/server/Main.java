package slaAuctions.server;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import slaAuctions.agents.Auctioneer;
import slaAuctions.agents.DoubleCustomer;
import slaAuctions.agents.DoubleProvider;
import slaAuctions.agents.DutchCustomer;
import slaAuctions.agents.DutchProvider;
import slaAuctions.agents.RevEnglishCustomer;
import slaAuctions.agents.RevEnglishProvider;
import slaAuctions.entities.Template;
import slaAuctions.providerBeans.AuctioneerBean;
import slaAuctions.providerBeans.ServerBean;

public class Main {

	public static void main(String[] args) throws Exception {
		/* Create app context */
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/Application.xml");
		
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
		
		for (Template t : parser.getCustomer().get("dutch")) {
			DutchCustomer c = new DutchCustomer(context, t);
			executor.execute(c);
		}
		for (Template t : parser.getProvider().get("dutch")) {
			DutchProvider p = new DutchProvider(context, t);
			executor.execute(p);
		}
		
		int size = parser.getCustomer().get("double").size() + parser.getProvider().get("double").size();
		CountDownLatch latch = new CountDownLatch(size);
		
		Auctioneer auctioneer = new Auctioneer(context, null, latch);
		executor.execute(auctioneer);

		for (Template t : parser.getCustomer().get("double")) {
			DoubleCustomer c = new DoubleCustomer(context, t, latch);
			executor.execute(c);
		}
		for (Template t : parser.getProvider().get("double")) {
			DoubleProvider p = new DoubleProvider(context, t, latch);
			executor.execute(p);
		}
		
		Thread.sleep(1000 * 60 * 2);
		executor.shutdown();
		System.out.println("WAITING FOR SHUTDOWN");
		executor.awaitTermination(1, TimeUnit.MINUTES);
		System.out.println("Total matches: " + bean.getMatches());
		context.destroy();
		System.exit(0);
	}

}
