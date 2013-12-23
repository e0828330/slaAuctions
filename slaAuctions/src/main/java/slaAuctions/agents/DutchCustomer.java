package slaAuctions.agents;

import java.util.Date;

import org.springframework.context.ApplicationContext;

import slaAuctions.customerBeans.DutchCustomerBean;
import slaAuctions.entities.Template;
import slaAuctions.exceptions.TransactionAbortedException;

public class DutchCustomer extends Agent {

	private int TIMEOUT = 60 * 1 * 1000; // 1 minute hardcoded for now

	
	public DutchCustomer(ApplicationContext context, Template template) {
		super(context, template);
	}
	
	Date start = new Date();

	public void run() {
		DutchCustomerBean bean = (DutchCustomerBean) context.getBean("dutchCustomerBean");
		while (true) {
			Template match = bean.waitForMatch(template);
			try {
				bean.writeMatch(match.getUid());
				break;
			} catch (TransactionAbortedException e) {
				System.out.println("Customer was to late :/");
			}
			if (start.getTime() + TIMEOUT < (new Date()).getTime()) {
				break;
			}
		}
	}

}
