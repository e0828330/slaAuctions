package slaAuctions.agents;

import java.util.Date;
import java.util.LinkedList;

import org.springframework.context.ApplicationContext;

import slaAuctions.customerBeans.RevEnglishCustomerBean;
import slaAuctions.entities.Template;
import slaAuctions.exceptions.TransactionAbortedException;

public class RevEnglishCustomer extends Agent {

	public RevEnglishCustomer(ApplicationContext context, Template template) {
		super(context, template, template.getCustomerId());
	}
	
	private int TIMEOUT = 1000 * 1; // 1 minute hard coded for now

	public void run() {
		RevEnglishCustomerBean bean = (RevEnglishCustomerBean) context.getBean("revEnglishCustomerBean");
		bean.writeTemplate(template);
		
		LinkedList<String> templates = new LinkedList<String>();
		Integer currentPrice = Integer.MAX_VALUE;
		
		Date start = new Date();
		
		Template match = bean.waitForMatch(template, Integer.MAX_VALUE);
		
		while(true) {
			Template tpl = bean.waitForMatch(template, 1000);
			if (tpl != null) {
				match = tpl;
			}
			if (match.getPrice() < currentPrice) {
				currentPrice = match.getPrice();
				template.setPrice(currentPrice);
				templates.addFirst(match.getUid());
			}
			if (start.getTime() + TIMEOUT < (new Date()).getTime()) {
				break;
			}
		}
		if (!templates.isEmpty()) {
			for (String templateId : templates)  {
				try {
					bean.writeMatch(templateId, id);
				} catch (TransactionAbortedException e) {
					System.out.println("Customer " + id + " was to late for template " + templateId);
				}
			}
		}
	}

}
