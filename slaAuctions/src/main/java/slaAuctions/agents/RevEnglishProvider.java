package slaAuctions.agents;

import java.util.Date;

import org.springframework.context.ApplicationContext;

import slaAuctions.entities.Template;
import slaAuctions.providerBeans.RevEnglishProviderBean;

public class RevEnglishProvider extends Agent {

	private int TIMEOUT = 60 * 5 * 1000; // 5 minutes hardcoded for now
	
	public RevEnglishProvider(ApplicationContext context, Template template) {
		super(context, template);
	}

	public void run() {
		RevEnglishProviderBean bean = (RevEnglishProviderBean) context.getBean("revEnglishProviderBean");
		
		Date start = new Date();
		bean.waitForCustomerTemplate(template);
		bean.writeTemplate(template);

		while(true) {
			if (bean.waitForMatch(template.getProviderId(), 5000)) {
				System.out.println(template.getProviderId() + " - SOLD!");
				break;
			}
			if ((new Date()).getTime() + TIMEOUT > start.getTime()) {
				break;
			}
			if (template.getCurrentValues().get("Price") > template.getMinValues().get("Price")) {
				template.getCurrentValues().put("Price", (int) Math.max(template.getMinValues().get("Price"), template.getCurrentValues().get("Price") * 0.9));
				bean.updateTemplate(template);
			}
			else {
				break;
			}
		}

		System.out.println(template.getProviderId() + " - DONE!");
	}

}
