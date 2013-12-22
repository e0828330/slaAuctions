package slaAuctions.agents;

import org.springframework.context.ApplicationContext;

import slaAuctions.customerBeans.DutchCustomerBean;
import slaAuctions.entities.Template;
import slaAuctions.exceptions.TransactionAbortedException;

public class DutchCustomer extends Agent {

	public DutchCustomer(ApplicationContext context, Template template) {
		super(context, template);
	}

	public void run() {
		DutchCustomerBean bean = (DutchCustomerBean) context.getBean("dutchCustomerBean");
		Template match = bean.waitForMatch(template);
		try {
			bean.writeMatch(match.getUid());
		} catch (TransactionAbortedException e) {
			System.out.println("Customer was to late :/");
		}
	}

}
