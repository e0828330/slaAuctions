package slaAuctions.agents;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.ApplicationContext;

import com.j_spaces.core.client.SQLQuery;

import slaAuctions.entities.DoubleAuctionTemplate;
import slaAuctions.entities.Template;
import slaAuctions.providerBeans.AuctioneerBean;

public class Auctioneer extends Agent {

	private int ROUNDTIME = 5000;
	
	private CountDownLatch latch;
	
	public Auctioneer(ApplicationContext context, Template template, CountDownLatch latch) {
		super(context, template);
		this.bean = (AuctioneerBean) context.getBean("auctioneerBean");
		this.latch = latch;
	}
	
	private AuctioneerBean bean;

	
	public void run() {
		try {
			System.out.println("Auctioneer blocking...");
			latch.await();
			System.out.println("Auctioneer has finished blocking...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		bean.receive();
		
		//Date start = new Date();
				
		/*while (true) {
			DoubleAuctionTemplate template = bean.receive();
			if (template.getCustomerId() != null) {
				// TODO save to local list
				System.out.println("Received customer template : " + template.getCustomerId());
			}
			else if (template.getProviderId() != null) {
				// TODO save to local list
				System.out.println("Received provider template : " + template.getProviderId());
			}
			
			if (start.getTime() + this.ROUNDTIME < (new Date()).getTime()) {
				System.out.println("Round is over, start making prices");
				break;
			}
		}*/
		
		//calculatePrices();
		
	}
	

	/**
	 * Calculate fixed prices for same templates
	 */
	private void calculatePrices() {
		
	}

}
