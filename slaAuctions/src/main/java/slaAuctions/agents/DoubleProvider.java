package slaAuctions.agents;

import java.util.concurrent.CountDownLatch;

import org.springframework.context.ApplicationContext;

import slaAuctions.entities.DoubleAuctionTemplate;
import slaAuctions.entities.PriceTemplate;
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
		PriceTemplate priceTemplate = bean.waitForTemplate(template.getProviderId());
		System.out.println("Provider: Received price :" + priceTemplate.getPrice());

		
		while(true) {
			if (bean.waitForMatch(priceTemplate.getProviderId(), 5000) == false) {
				System.out.println("No match");
				break;
			}
			
		}		
		
	}

}
