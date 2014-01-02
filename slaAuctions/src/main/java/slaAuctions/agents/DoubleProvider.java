package slaAuctions.agents;

import org.springframework.context.ApplicationContext;

import slaAuctions.customerBeans.DoubleCustomerBean;
import slaAuctions.entities.DoubleAuctionTemplate;
import slaAuctions.entities.Template;
import slaAuctions.providerBeans.DoubleProviderBean;

public class DoubleProvider extends Agent {

	public DoubleProvider(ApplicationContext context, Template template) {
		super(context, template);
	}

	public void run() {
		DoubleProviderBean bean = (DoubleProviderBean) context.getBean("doubleProviderBean");
		System.out.println("Write template into space auctioneer-space: uid =  " + template.getProviderId());
		bean.writeAuctioneerTemplate(new DoubleAuctionTemplate(template));		
	}

}
