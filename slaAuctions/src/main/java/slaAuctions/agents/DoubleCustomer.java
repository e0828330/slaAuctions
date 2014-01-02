package slaAuctions.agents;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import slaAuctions.customerBeans.DoubleCustomerBean;
import slaAuctions.entities.DoubleAuctionTemplate;
import slaAuctions.entities.PriceTemplate;
import slaAuctions.entities.Template;

public class DoubleCustomer extends Agent {

	Logger logger = Logger.getLogger(getClass());
	
	private CountDownLatch latch;
	
	public DoubleCustomer(ApplicationContext context, Template template, CountDownLatch latch) {
		super(context, template);
		this.latch = latch;
	}

	public void run() {
		DoubleCustomerBean bean = (DoubleCustomerBean) context.getBean("doubleCustomerBean");
		System.out.println("Write template into space auctioneer-space: uid =  " + template.getCustomerId());
		bean.writeAuctioneerTemplate(new DoubleAuctionTemplate(template));
		latch.countDown();
		PriceTemplate priceTemplate = bean.waitForTemplate(template.getCustomerId());
		System.out.println("Customer: Received price :" + priceTemplate.getPrice());
	}

}
