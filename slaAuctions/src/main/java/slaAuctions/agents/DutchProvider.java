package slaAuctions.agents;

import java.util.Date;

import org.springframework.context.ApplicationContext;

import slaAuctions.entities.Template;
import slaAuctions.providerBeans.DutchProviderBean;

public class DutchProvider extends Agent {
	
	private int TIMEOUT = 60 * 20; // 1 minutes hardcoded for now

	public DutchProvider(ApplicationContext context, Template template) {
		super(context, template, template.getProviderId());
	}

	public void run() {
		DutchProviderBean bean = (DutchProviderBean) context.getBean("dutchProviderBean");
		Date start = new Date();
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
				try {
					bean.updateTemplate(template);
				}
				catch (Exception e) {
					// Template gone so match?
					bean.waitForMatch(template.getProviderId(), 5000);
					break;
				}
			}
		}
		
	}

}
