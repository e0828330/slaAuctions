package slaAuctions.providerBeans;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import slaAuctions.entities.Template;

import com.j_spaces.core.client.SQLQuery;

@Transactional
public class RevEnglishProviderBean {
	@GigaSpaceContext
	private GigaSpace space;

	public void waitForCustomerTemplate(Template tpl) {
		String queryString = "";
		for (String key : tpl.getCurrentValues().keySet()) {
			if (!queryString.isEmpty()) {
				queryString += " AND ";
			}
			queryString += "minValues." + key + " >= " + tpl.getCurrentValues().get(key) +
						   " AND maxValues." + key + " <= " + tpl.getCurrentValues().get(key);
		}
		System.out.println(queryString);
		space.read(new SQLQuery<Template>(Template.class, queryString), Integer.MAX_VALUE);
	}
	
	public void writeTemplate(Template tpl) {
		space.write(tpl);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateTemplate(Template template) {
		space.take(new SQLQuery<Template>(Template.class, "providerId = ?", template.getProviderId()), Integer.MAX_VALUE);
		space.write(template);
	}

	public boolean waitForMatch(Integer providerId, Integer timeout) {
		Template result = space.take(new SQLQuery<Template>(Template.class, "providerId = ?", providerId), timeout);
		return result != null;
	}
	
}
