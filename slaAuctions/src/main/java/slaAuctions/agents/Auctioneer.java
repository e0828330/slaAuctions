package slaAuctions.agents;

import org.springframework.context.ApplicationContext;

import slaAuctions.entities.DoubleAuctionTemplate;
import slaAuctions.entities.Template;
import slaAuctions.providerBeans.AuctioneerBean;

public class Auctioneer extends Agent {

	public Auctioneer(ApplicationContext context, Template template) {
		super(context, template);
		this.bean = (AuctioneerBean) context.getBean("auctioneerBean");
	}
	
	private AuctioneerBean bean;

	
	public void run() {
		while (true) {
			DoubleAuctionTemplate template = bean.receive();
			if (template.getCustomerId() != null) {
				System.out.println("Received customer template : " + template.getCustomerId());
			}
			else if (template.getProviderId() != null) {
				System.out.println("Received provider template : " + template.getProviderId());
			}
		}
	}


}
