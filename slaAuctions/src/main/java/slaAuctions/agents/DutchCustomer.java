package slaAuctions.agents;

import java.util.Date;

import org.springframework.context.ApplicationContext;

import slaAuctions.customerBeans.DutchCustomerBean;
import slaAuctions.entities.Template;
import slaAuctions.exceptions.TransactionAbortedException;

public class DutchCustomer extends Agent {

	private int TIMEOUT = 60 * 20; // 1 minute hardcoded for now

	
	public DutchCustomer(ApplicationContext context, Template template) {
		super(context, template, template.getCustomerId());
	}
	
	Date start = new Date();

	public void run() {
		DutchCustomerBean bean = (DutchCustomerBean) context.getBean("dutchCustomerBean");
		while (true) {
			Template match = bean.waitForMatch(template);
			try {
				bean.writeMatch(match.getUid(), id);
				break;
			} catch (TransactionAbortedException e) {
				System.out.println("Customer " + id + " was to late for template " + match.getUid());
			}
			if (start.getTime() + TIMEOUT < (new Date()).getTime()) {
				break;
			}
		}
	}

}
