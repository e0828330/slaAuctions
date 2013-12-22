package slaAuctions.agents;

import java.util.Date;

import org.springframework.context.ApplicationContext;

import slaAuctions.customerBeans.RevEnglishCustomerBean;
import slaAuctions.entities.Template;

public class RevEnglishCustomer extends Agent {

	public RevEnglishCustomer(ApplicationContext context, Template template) {
		super(context, template);
	}
	
	private int TIMEOUT = 60 * 1 * 1000; // 5 minutes hardcoded for now

	public void run() {
		RevEnglishCustomerBean bean = (RevEnglishCustomerBean) context.getBean("revEnglishCustomerBean");
		bean.writeTemplate(template);
		
		String bestTpl = "";
		Integer currentPrice = Integer.MAX_VALUE;
		
		Date start = new Date();
		
		while(true) {
			Template match = bean.waitForMatch(template);
			System.out.println("Found template from " + match.getProviderId());
			System.out.println("Price is " + match.getCurrentValues().get("Price"));
			if (match.getCurrentValues().get("Price") < currentPrice) {
				currentPrice = match.getCurrentValues().get("Price");
				template.getMaxValues().put("Price", currentPrice);
				bestTpl = match.getUid();
				System.out.println("best is "+ bestTpl + " price = " + currentPrice);
			}
			if (start.getTime() + TIMEOUT < (new Date()).getTime()) {
				System.out.println("customer time out");
				break;
			}
		}
		if (!bestTpl.isEmpty()) {
			bean.writeMatch(bestTpl);
		}
	}

}
