package slaAuctions.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
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
		/* Check command line */
		if (args.length != 1) {
			System.err.println("Please supply a config file");
			System.exit(1);
		}
		
		/* Create app context */
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/Application.xml");
		
		ServerBean bean = (ServerBean) context.getBean("serverBean");

		/* Wait for match notifications */
		bean.doNotify();
		
		ExecutorService executor = Executors.newCachedThreadPool();
		
		/* Parse config file */
		ConfigParser parser = new ConfigParser();
		parser.doParse(args[0]);
		
		/* Starts agents */
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
		
		/* Wait for auctions to finish */
		Thread.sleep(parser.getTimeout());
		executor.shutdown();
		System.out.println("WAITING FOR SHUTDOWN");
		executor.awaitTermination(10, TimeUnit.SECONDS);
		
		/* Evaluate matches and create statistics */
		int matchedDoubleCustomerDoubleProvider = 0;
		int matchedDoubleCustomerDutchProvider = 0;
		int matchedDoubleCustomerRevEnglishProvider = 0;
		int matchedDutchCustomerDoubleProvider = 0;
		int matchedDutchCustomerDutchProvider = 0;
		int matchedDutchCustomerRevEnglishProvider = 0;
		int matchedRevEnglishCustomerDoubleProvider = 0;
		int matchedRevEnglishCustomerDutchProvider = 0;
		int matchedRevEnglishCustomerRevEnglishProvider = 0;
		
		for (Match m : bean.getMatches()) {
			System.out.println("Match: provider " + m.getProviderId() + " and customer " + m.getCustomerId());
			if (m.getCustomerId().startsWith("double")) {
				if (m.getProviderId().startsWith("double")) {
					matchedDoubleCustomerDoubleProvider++;
				}
				if (m.getProviderId().startsWith("dutch")) {
					matchedDoubleCustomerDutchProvider++;
				}
				if (m.getProviderId().startsWith("english")) {
					matchedDoubleCustomerRevEnglishProvider++;
				}
			}
			if (m.getCustomerId().startsWith("dutch")) {
				if (m.getProviderId().startsWith("double")) {
					matchedDutchCustomerDoubleProvider++;
				}
				if (m.getProviderId().startsWith("dutch")) {
					matchedDutchCustomerDutchProvider++;
				}
				if (m.getProviderId().startsWith("english")) {
					matchedDutchCustomerRevEnglishProvider++;
				}
			}
			if (m.getCustomerId().startsWith("english")) {
				if (m.getProviderId().startsWith("double")) {
					matchedRevEnglishCustomerDoubleProvider++;
				}
				if (m.getProviderId().startsWith("dutch")) {
					matchedRevEnglishCustomerDutchProvider++;
				}
				if (m.getProviderId().startsWith("english")) {
					matchedRevEnglishCustomerRevEnglishProvider++;
				}
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
		
		int matchedDoubleCustomers = matchedDoubleCustomerDoubleProvider + matchedDoubleCustomerDutchProvider + matchedDoubleCustomerRevEnglishProvider;
		int matchedDutchCustomers = matchedDutchCustomerDoubleProvider + matchedDutchCustomerDutchProvider + matchedDutchCustomerRevEnglishProvider;
		int matchedrevEnglishCustomers = matchedRevEnglishCustomerDoubleProvider + matchedRevEnglishCustomerDutchProvider + matchedRevEnglishCustomerRevEnglishProvider;
		int matchedDoubleProviders = matchedDoubleCustomerDoubleProvider + matchedDutchCustomerDoubleProvider + matchedRevEnglishCustomerDoubleProvider;
		int matchedDutchProviders = matchedDoubleCustomerDutchProvider + matchedDutchCustomerDutchProvider + matchedRevEnglishCustomerDutchProvider;
		int matchedRevEnglishProviders = matchedDoubleCustomerRevEnglishProvider + matchedDutchCustomerRevEnglishProvider + matchedRevEnglishCustomerRevEnglishProvider;
		
		int mixedMatches = matchedDoubleCustomerDutchProvider + matchedDoubleCustomerRevEnglishProvider + matchedDutchCustomerDoubleProvider + matchedDutchCustomerRevEnglishProvider + matchedRevEnglishCustomerDoubleProvider + matchedRevEnglishCustomerDutchProvider;
		
		BigDecimal totalMatchPercentage = totalAgents == 0 ? BigDecimal.ZERO : new BigDecimal(100 * totalMatchedAgents).divide(new BigDecimal(totalAgents), 2, RoundingMode.HALF_UP);
		
		BigDecimal doubleCustomersMatchPercentage = doubleCustomers == 0 ? BigDecimal.ZERO : new BigDecimal(100 * matchedDoubleCustomers).divide(new BigDecimal(doubleCustomers), 2, RoundingMode.HALF_UP);
		BigDecimal dutchCustomersMatchPercentage = dutchCustomers == 0 ? BigDecimal.ZERO : new BigDecimal(100 * matchedDutchCustomers).divide(new BigDecimal(dutchCustomers), 2, RoundingMode.HALF_UP);
		BigDecimal revEnglishCustomersMatchPercentage = revEnglishCustomers == 0 ? BigDecimal.ZERO : new BigDecimal(100 * matchedrevEnglishCustomers).divide(new BigDecimal(revEnglishCustomers), 2, RoundingMode.HALF_UP);
		BigDecimal doubleProvidersMatchPercentage = doubleProviders == 0 ? BigDecimal.ZERO : new BigDecimal(100 * matchedDoubleProviders).divide(new BigDecimal(doubleProviders), 2, RoundingMode.HALF_UP);
		BigDecimal dutchProvidersMatchPercentage = dutchProviders == 0 ? BigDecimal.ZERO : new BigDecimal(100 * matchedDutchProviders).divide(new BigDecimal(dutchProviders), 2, RoundingMode.HALF_UP);
		BigDecimal revEnglishProvidersMatchPercentage = revEnglishProviders == 0 ? BigDecimal.ZERO : new BigDecimal(100 * matchedRevEnglishProviders).divide(new BigDecimal(revEnglishProviders), 2, RoundingMode.HALF_UP);

		BigDecimal mixedMatchPercentage = totalMatchedAgents == 0 ? BigDecimal.ZERO : new BigDecimal(100 * mixedMatches).divide(new BigDecimal(bean.getMatches().size()), 2, RoundingMode.HALF_UP);
		
		System.out.println("total match percentage: " + totalMatchPercentage + "%\n");

		System.out.println("double customer match percentage: " + doubleCustomersMatchPercentage + "%");
		System.out.println("dutch customer match percentage: " + dutchCustomersMatchPercentage + "%");
		System.out.println("english customer match percentage: " + revEnglishCustomersMatchPercentage + "%");
		System.out.println("double provider match percentage: " + doubleProvidersMatchPercentage + "%");
		System.out.println("dutch provider match percentage: " + dutchProvidersMatchPercentage + "%");
		System.out.println("english provider match percentage: " + revEnglishProvidersMatchPercentage + "%\n");

		System.out.println("mixed match percentage: " + mixedMatchPercentage + "%");
		
		System.out.println();
		System.out.println("match table:");
		System.out.println(String.format("%10s|%10s|%10s|%10s", "", "double p", "dutch p", "english p"));
		System.out.println("----------+----------+----------+----------");
		System.out.println(String.format("%10s|%10d|%10d|%10d", "double c", matchedDoubleCustomerDoubleProvider, matchedDoubleCustomerDutchProvider, matchedDoubleCustomerRevEnglishProvider));
		System.out.println("----------+----------+----------+----------");
		System.out.println(String.format("%10s|%10d|%10d|%10d", "dutch c", matchedDutchCustomerDoubleProvider, matchedDutchCustomerDutchProvider, matchedDutchCustomerRevEnglishProvider));
		System.out.println("----------+----------+----------+----------");
		System.out.println(String.format("%10s|%10d|%10d|%10d", "english c", matchedRevEnglishCustomerDoubleProvider, matchedRevEnglishCustomerDutchProvider, matchedRevEnglishCustomerRevEnglishProvider));
		
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("matches.csv", true)));
		
		writer.print("\n\n");
		writer.println("min properties,max properties,double provider,dutch provider,reverse english provider,double customer,dutch customer,reverse english customer");
		writer.println(parser.getMinProperties()+","+parser.getMaxProperties()+","+doubleProviders+","+dutchProviders+","+revEnglishProviders+","+doubleCustomers+","+dutchCustomers+","+revEnglishCustomers);
		writer.println();
		writer.println(",double provider,dutch provider,reverse english provider");
		writer.println("double customer,"+matchedDoubleCustomerDoubleProvider+","+matchedDoubleCustomerDutchProvider+","+matchedDoubleCustomerRevEnglishProvider);
		writer.println("dutch customer,"+matchedDutchCustomerDoubleProvider+","+matchedDutchCustomerDutchProvider+","+matchedDutchCustomerRevEnglishProvider);
		writer.println("reverse english customer,"+matchedRevEnglishCustomerDoubleProvider+","+matchedRevEnglishCustomerDutchProvider+","+matchedRevEnglishCustomerRevEnglishProvider);
		
		writer.close();
		
		context.destroy();
		System.exit(0);
	}

}
