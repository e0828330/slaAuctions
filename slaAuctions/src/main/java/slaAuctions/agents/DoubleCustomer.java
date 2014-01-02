package slaAuctions.agents;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import slaAuctions.customerBeans.DoubleCustomerBean;
import slaAuctions.entities.DoubleAuctionTemplate;
import slaAuctions.entities.Template;

public class DoubleCustomer extends Agent {

	Logger logger = Logger.getLogger(getClass());
	
	public DoubleCustomer(ApplicationContext context, Template template) {
		super(context, template);
	}

	public void run() {
		System.out.println("Doublecustomer");
		DoubleCustomerBean bean = (DoubleCustomerBean) context.getBean("doubleCustomerBean");
		this.logger.info("Write template into space auctioneer-space");
		//bean.writeAuctioneerTemplate((DoubleAuctionTemplate) template);
	}

}
