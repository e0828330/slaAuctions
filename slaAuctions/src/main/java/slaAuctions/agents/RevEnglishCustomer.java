package slaAuctions.agents;

import java.util.Date;

import org.springframework.context.ApplicationContext;

import slaAuctions.customerBeans.RevEnglishCustomerBean;
import slaAuctions.entities.Template;

public class RevEnglishCustomer extends Agent {

	public RevEnglishCustomer(ApplicationContext context, Template template) {
		super(context, template);
	}
	
	private int TIMEOUT = 60 * 5 * 1000; // 5 minutes hardcoded for now

	public void run() {
		RevEnglishCustomerBean bean = (RevEnglishCustomerBean) context.getBean("revEnglishCustomerBean");
		bean.writeTemplate(template);
		
		String bestTpl = "";
		Integer currentPrice = Integer.MAX_VALUE;
		
		Date start = new Date();
		
		while(true) {
			Template match = bean.waitForMatch(template);
			if (match.getCurrentValues().get("Price") < currentPrice) {
				currentPrice = match.getCurrentValues().get("Price");
				template.getMaxValues().put("Price", currentPrice);
				bestTpl = match.getUid();
			}
			if ((new Date()).getTime() + TIMEOUT > start.getTime()) {
				break;
			}
		}
		if (!bestTpl.isEmpty()) {
			bean.writeMatch(bestTpl);
		}
	}

}
