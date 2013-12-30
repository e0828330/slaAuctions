package slaAuctions.agents;

import org.springframework.context.ApplicationContext;

import slaAuctions.customerBeans.DoubleCustomerBean;
import slaAuctions.entities.Template;

public class DoubleCustomer extends Agent {

	public DoubleCustomer(ApplicationContext context, Template template) {
		super(context, template);
	}

	public void run() {
		DoubleCustomerBean bean = (DoubleCustomerBean) context.getBean("doubleCustomerBean");
		System.out.println("Created doubleCustomerBean");
	}

}
