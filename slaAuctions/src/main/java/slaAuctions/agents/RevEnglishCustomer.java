package slaAuctions.agents;

import java.util.Date;

import org.springframework.context.ApplicationContext;

import slaAuctions.customerBeans.RevEnglishCustomerBean;
import slaAuctions.entities.Template;
import slaAuctions.exceptions.TransactionAbortedException;

public class RevEnglishCustomer extends Agent {

	public RevEnglishCustomer(ApplicationContext context, Template template) {
		super(context, template);
	}
	
	private int TIMEOUT = 30 * 1 * 1000; // 5 minutes hardcoded for now

	public void run() {
		RevEnglishCustomerBean bean = (RevEnglishCustomerBean) context.getBean("revEnglishCustomerBean");
		bean.writeTemplate(template);
		
		String bestTpl = "";
		Integer currentPrice = Integer.MAX_VALUE;
		
		Date start = new Date();
		
		while(true) {
			Template match = bean.waitForMatch(template);
			/*if (match.getCurrentValues().get("Price") < currentPrice) {
				System.out.println("new best price = " + match.getCurrentValues().get("Price"));
				currentPrice = match.getCurrentValues().get("Price");
				template.getMaxValues().put("Price", currentPrice);
				bestTpl = match.getUid();
			}*/
			if (start.getTime() + TIMEOUT < (new Date()).getTime()) {
				System.out.println("Customer time out");
				break;
			}
		}
		if (!bestTpl.isEmpty()) {
			try {
				bean.writeMatch(bestTpl);
			} catch (TransactionAbortedException e) {
				System.out.println("To late :/");
			}
		}
	}

}
