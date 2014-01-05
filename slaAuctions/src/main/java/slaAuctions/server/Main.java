package slaAuctions.server;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import slaAuctions.entities.Match;
import slaAuctions.entities.Template;
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
		parser.doParse(args[0]);
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
		
		Thread.sleep(parser.getTimeout());
		executor.shutdown();
		System.out.println("WAITING FOR SHUTDOWN");
		executor.awaitTermination(10, TimeUnit.SECONDS);
		
		int matchedDoubleCustomers = 0;
		int matchedDoubleProviders = 0;
		int matchedDutchCustomers = 0;
		int matchedDutchProviders = 0;
		int matchedrevEnglishCustomers = 0;
		int matchedRevEnglishProviders = 0;
		int mixedMatches = 0;
		
		for (Match m : bean.getMatches()) {
			System.out.println("Match: provider " + m.getProviderId() + " and customer " + m.getCustomerId());
			String type = "";
			if (m.getCustomerId().startsWith("dutch")) {
				matchedDutchCustomers++;
				type = "dutch";
			}
			if (m.getCustomerId().startsWith("double")) {
				matchedDoubleCustomers++;
				type = "double";
			}
			if (m.getCustomerId().startsWith("english")) {
				matchedrevEnglishCustomers++;
				type = "english";
			}
			if (m.getProviderId().startsWith("dutch")) {
				matchedDutchProviders++;
				if (!type.equals("dutch"))
					mixedMatches++;
			}
			if (m.getProviderId().startsWith("double")) {
				matchedDoubleProviders++;
				if (!type.equals("double"))
					mixedMatches++;
			}
			if (m.getProviderId().startsWith("english")) {
				matchedRevEnglishProviders++;
				if (!type.equals("english"))
					mixedMatches++;
			}
		}
		
		int doubleCustomers = parser.getCustomer().get("double").size();
		int doubleProviders = parser.getProvider().get("double").size();
		int dutchCustomers = parser.getCustomer().get("dutch").size();
		int dutchProviders = parser.getProvider().get("dutch").size();
		int revEnglishCustomers = parser.getCustomer().get("english").size();
		int revEnglishProviders = parser.getProvider().get("english").size();
		
		int totalCustomers = doubleCustomers + dutchCustomers + revEnglishCustomers;
		int totalProviders = doubleProviders + dutchProviders + revEnglishProviders;
		
		int totalDouble = doubleCustomers + doubleProviders;
		int totalDutch = dutchCustomers + dutchProviders;
		int totalEnglish = revEnglishCustomers + revEnglishProviders;
		
		int totalAgents = totalCustomers + totalProviders;
		int totalMatchedAgents = 2 * bean.getMatches().size();
		
		BigDecimal totalMatchPercentage = totalAgents == 0 ? BigDecimal.ZERO : new BigDecimal(100 * totalMatchedAgents).divide(new BigDecimal(totalAgents), 2, RoundingMode.HALF_UP);
		
		BigDecimal doubleCustomersMatchPercentage = doubleCustomers == 0 ? BigDecimal.ZERO : new BigDecimal(100 * matchedDoubleCustomers).divide(new BigDecimal(doubleCustomers), 2, RoundingMode.HALF_UP);
		BigDecimal dutchCustomersMatchPercentage = dutchCustomers == 0 ? BigDecimal.ZERO : new BigDecimal(100 * matchedDutchCustomers).divide(new BigDecimal(dutchCustomers), 2, RoundingMode.HALF_UP);
		BigDecimal revEnglishCustomersMatchPercentage = revEnglishCustomers == 0 ? BigDecimal.ZERO : new BigDecimal(100 * matchedrevEnglishCustomers).divide(new BigDecimal(revEnglishCustomers), 2, RoundingMode.HALF_UP);
		BigDecimal doubleProvidersMatchPercentage = doubleProviders == 0 ? BigDecimal.ZERO : new BigDecimal(100 * matchedDoubleProviders).divide(new BigDecimal(doubleProviders), 2, RoundingMode.HALF_UP);
		BigDecimal dutchProvidersMatchPercentage = dutchProviders == 0 ? BigDecimal.ZERO : new BigDecimal(100 * matchedDutchProviders).divide(new BigDecimal(dutchProviders), 2, RoundingMode.HALF_UP);
		BigDecimal revEnglishProvidersMatchPercentage = revEnglishProviders == 0 ? BigDecimal.ZERO : new BigDecimal(100 * matchedRevEnglishProviders).divide(new BigDecimal(revEnglishProviders), 2, RoundingMode.HALF_UP);

		BigDecimal mixedMatchPercentage = totalMatchedAgents == 0 ? BigDecimal.ZERO : new BigDecimal(100 * mixedMatches).divide(new BigDecimal(totalMatchedAgents), 2, RoundingMode.HALF_UP);
		
		System.out.println("total match percentage: " + totalMatchPercentage + "%\n");

		System.out.println("double customer match percentage: " + doubleCustomersMatchPercentage + "%");
		System.out.println("dutch customer match percentage: " + dutchCustomersMatchPercentage + "%");
		System.out.println("english customer match percentage: " + revEnglishCustomersMatchPercentage + "%");
		System.out.println("double provider match percentage: " + doubleProvidersMatchPercentage + "%");
		System.out.println("dutch provider match percentage: " + dutchProvidersMatchPercentage + "%");
		System.out.println("english provider match percentage: " + revEnglishProvidersMatchPercentage + "%\n");

		System.out.println("mixed match percentage: " + mixedMatchPercentage + "%");
		
		context.destroy();
		System.exit(0);
	}

}
