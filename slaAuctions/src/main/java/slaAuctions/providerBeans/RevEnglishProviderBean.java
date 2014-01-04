package slaAuctions.providerBeans;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import slaAuctions.entities.Match;
import slaAuctions.entities.Template;

import com.j_spaces.core.client.SQLQuery;

@Transactional
public class RevEnglishProviderBean {
	@GigaSpaceContext
	private GigaSpace space;

	public void waitForCustomerTemplate(Template tpl) {
		String queryString = "";

		int i = 0;
		for (i = 0; i < 9; i++) {
			Integer current;
			try {
				current = (Integer) PropertyUtils.getSimpleProperty(tpl, "property" + i + "_current");
				if (current != null) {
					if (!queryString.isEmpty()) {
						queryString += " AND ";
					}
					queryString += " property" + i + "_min <= " + current + " AND ";
					queryString += "property" + i + "_max <= " + current;
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			
		}
		
		space.read(new SQLQuery<Template>(Template.class, queryString), Integer.MAX_VALUE);
	}
	
	public void writeTemplate(Template tpl) {
		space.write(tpl);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateTemplate(Template template) {
		space.take(new SQLQuery<Template>(Template.class, "providerId = ?", template.getProviderId()), Integer.MAX_VALUE);
		space.write(template, Integer.MAX_VALUE);
	}

	public boolean waitForMatch(String providerId, Integer timeout) {
		Match result = space.take(new SQLQuery<Match>(Match.class, "providerId = ?", providerId), timeout);
		return result != null;
	}
	
}
