package slaAuctions.agents;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import slaAuctions.customerBeans.DoubleCustomerBean;
import slaAuctions.entities.DoubleAuctionTemplate;
import slaAuctions.entities.PriceTemplate;
import slaAuctions.entities.Template;
import slaAuctions.exceptions.TransactionAbortedException;

public class DoubleCustomer extends Agent {

	Logger logger = Logger.getLogger(getClass());
	
	private CountDownLatch latch;
	
	private int TIMEOUT = 60 * 1 * 1000; // 1 minute hardcoded for now
	
	public DoubleCustomer(ApplicationContext context, Template template, CountDownLatch latch) {
		super(context, template);
		this.latch = latch;
	}

	public void run() {
		DoubleCustomerBean bean = (DoubleCustomerBean) context.getBean("doubleCustomerBean");
		//System.out.println("Write template into space auctioneer-space: uid =  " + template.getCustomerId());
		bean.writeAuctioneerTemplate(new DoubleAuctionTemplate(template));
		latch.countDown();
		PriceTemplate priceTemplate = bean.waitForPriceTemplate(template.getCustomerId());
		//System.out.println("Customer: Received price :" + priceTemplate.getPrice());
		
		Date start = new Date();
		
		while (true) {
			Template match = bean.waitForMatch((Template) priceTemplate);
			try {
				bean.writeMatch(match.getUid());
				break;
			} catch (TransactionAbortedException e) {
				System.out.println("Customer was to late :/");
			}
			if (start.getTime() + TIMEOUT < (new Date()).getTime()) {
				break;
			}
		}		
		
	}

}
