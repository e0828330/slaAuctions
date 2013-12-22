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
		System.out.println("Provider nr. " + template.getProviderId() + " FOUND CUSTOMER");
		bean.writeTemplate(template);

		while(true) {
			if (bean.waitForMatch(template.getProviderId(), 5000)) {
				break;
			}
			if (start.getTime() + TIMEOUT < (new Date()).getTime()) {
				break;
			}
			if (template.getPrice() > template.getPrice_min()) {
				template.setPrice((int) Math.max(template.getPrice_min(), template.getPrice() * 0.9));
				bean.updateTemplate(template);
			}
		}
	}

}
