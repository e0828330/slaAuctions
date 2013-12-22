package slaAuctions.providerBeans;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.j_spaces.core.client.SQLQuery;

import slaAuctions.entities.Match;
import slaAuctions.entities.Template;

@Transactional
public class DutchProviderBean {
	@GigaSpaceContext
	private GigaSpace space;
	
	public void writeTemplate(Template tpl) {
		space.write(tpl);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateTemplate(Template template) {
		space.take(new SQLQuery<Template>(Template.class, "providerId = ?", template.getProviderId()), Integer.MAX_VALUE);
		space.write(template, Integer.MAX_VALUE);
	}

	public boolean waitForMatch(Integer providerId, Integer timeout) {
		Match result = space.take(new SQLQuery<Match>(Match.class, "providerId = ?", providerId), timeout);
		return result != null;
	}
}
