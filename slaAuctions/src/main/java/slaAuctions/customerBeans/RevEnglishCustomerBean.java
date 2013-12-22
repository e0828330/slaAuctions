package slaAuctions.customerBeans;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.j_spaces.core.client.SQLQuery;

import slaAuctions.entities.Match;
import slaAuctions.entities.Template;

@Transactional
public class RevEnglishCustomerBean {
	@GigaSpaceContext
	private GigaSpace space;

	public void writeTemplate(Template tpl) {
		space.write(tpl);
	}
	
	public Template waitForMatch(Template tpl) {
		String queryString = "";
		for (String key : tpl.getMinValues().keySet()) {
			if (!queryString.isEmpty()) {
				queryString += " AND ";
			}
			queryString += "currentValues." + key + " BETWEEN " +  tpl.getMinValues().get(key) + " AND " + tpl.getMaxValues().get(key);
		}
		System.out.println(queryString);
		return space.take(new SQLQuery<Template>(Template.class, queryString), Integer.MAX_VALUE);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void writeMatch(String tplId) {
		Template tpl = space.takeById(Template.class, tplId, Integer.MAX_VALUE);
		space.write(new Match(tpl.getProviderId()));
		System.out.println("MATCH for " + tpl.getProviderId());
	}
}
