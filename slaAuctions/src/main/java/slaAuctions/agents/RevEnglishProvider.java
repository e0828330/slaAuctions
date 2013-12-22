package slaAuctions.agents;

import java.util.Date;

import org.springframework.context.ApplicationContext;

import slaAuctions.entities.Template;
import slaAuctions.providerBeans.RevEnglishProviderBean;

public class RevEnglishProvider extends Agent {

	private int TIMEOUT = 60 * 1 * 1000; // 5 minutes hardcoded for now
	
	public RevEnglishProvider(ApplicationContext context, Template template) {
		super(context, template);
	}

	public void run() {
		RevEnglishProviderBean bean = (RevEnglishProviderBean) context.getBean("revEnglishProviderBean");
		
		Date start = new Date();
		bean.waitForCustomerTemplate(template);
		System.out.println(template.getProviderId() + " FOUND CUSTOMER");
		bean.writeTemplate(template);
		System.out.println(template.getProviderId() + " WROTE TEMPLATE");

		while(true) {
			if (bean.waitForMatch(template.getProviderId(), 5000)) {
				System.out.println(template.getProviderId() + " - SOLD!");
				break;
			}
			System.out.println(template.getProviderId() + " - no match moving on...");
			if (start.getTime() + TIMEOUT < (new Date()).getTime()) {
				System.out.println("timed out");
				break;
			}
			if (template.getCurrentValues().get("Price") > template.getMinValues().get("Price")) {
				template.getCurrentValues().put("Price", (int) Math.max(template.getMinValues().get("Price"), template.getCurrentValues().get("Price") * 0.9));
				bean.updateTemplate(template);
				System.out.println(template.getProviderId() + " UDPATED TEMPLATE price = " + template.getCurrentValues().get("Price"));
			}
			else {
				System.out.println(template.getProviderId() + " reached min price = " + template.getCurrentValues().get("Price"));
			}
		}

		System.out.println(template.getProviderId() + " - DONE!");
	}

}
