package slaAuctions.agents;

import java.util.concurrent.CountDownLatch;

import org.springframework.context.ApplicationContext;

import slaAuctions.entities.DoubleAuctionTemplate;
import slaAuctions.entities.Template;
import slaAuctions.providerBeans.DoubleProviderBean;

public class DoubleProvider extends Agent {

	private CountDownLatch latch;
	
	public DoubleProvider(ApplicationContext context, Template template, CountDownLatch latch) {
		super(context, template);
		this.latch = latch;
	}

	public void run() {
		DoubleProviderBean bean = (DoubleProviderBean) context.getBean("doubleProviderBean");
		System.out.println("Write template into space auctioneer-space: uid =  " + template.getProviderId());
		bean.writeAuctioneerTemplate(new DoubleAuctionTemplate(template));	
		this.latch.countDown();
	}

}
