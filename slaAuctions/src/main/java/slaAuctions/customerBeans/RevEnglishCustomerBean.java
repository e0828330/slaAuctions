package slaAuctions.customerBeans;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.j_spaces.core.client.SQLQuery;

import slaAuctions.entities.Match;
import slaAuctions.entities.Template;
import slaAuctions.exceptions.TransactionAbortedException;

@Transactional
public class RevEnglishCustomerBean {
	@GigaSpaceContext
	private GigaSpace space;

	public void writeTemplate(Template tpl) {
		space.write(tpl);
	}
	
	public Template waitForMatch(Template tpl, int timeout) {
		String queryString = "";
		
		int i = 0;
		for (i = 0; i < 9; i++) {
			Integer min;
			Integer max;
			try {
				min = (Integer) PropertyUtils.getSimpleProperty(tpl, "property" + i + "_min");
				max = (Integer) PropertyUtils.getSimpleProperty(tpl, "property" + i + "_max");
				if (min != null && max != null) {
					if (!queryString.isEmpty()) {
						queryString += " AND ";
					}
					queryString += " property" + i + "_current BETWEEN " + (min-1) + " AND " + (max+1);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			
		}
		
		queryString += " AND price <= " + tpl.getPrice_max();
		
		//System.out.println("customer query: " + queryString);

		return space.read(new SQLQuery<Template>(Template.class, queryString), timeout);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void writeMatch(String tplId, String customerId) throws TransactionAbortedException {
		Template tpl = space.takeById(Template.class, tplId);
		if (tpl == null) {
			throw new TransactionAbortedException("Template no longer in the space!");
		}
		space.write(new Match(tpl.getProviderId(), customerId));
	}
}
