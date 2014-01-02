package slaAuctions.providerBeans;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.transaction.annotation.Transactional;

import slaAuctions.entities.DoubleAuctionTemplate;
import slaAuctions.entities.PriceTemplate;
import slaAuctions.entities.Template;

import com.j_spaces.core.client.SQLQuery;

@Transactional
public class AuctioneerBean {

	@GigaSpaceContext
	private GigaSpace space;
	
	Logger logger = Logger.getLogger(getClass());
	
	public int getTotalNumber() {
		int totalTemplateNumber = space.count(new DoubleAuctionTemplate());
		return totalTemplateNumber;
	}
	
	public DoubleAuctionTemplate getDoubleAucationTemplate() {
		return space.take(new DoubleAuctionTemplate(), Integer.MAX_VALUE);
	}

	public DoubleAuctionTemplate[] receiveSameTemplates(Template tpl) {
		String queryString = "";
		
		int i = 0;
		for (i = 0; i < 9; i++) {
			Integer min;
			Integer max;
			try {
				min = (Integer) PropertyUtils.getSimpleProperty(tpl, "property" + i + "_min");
				max = (Integer) PropertyUtils.getSimpleProperty(tpl, "property" + i + "_max");
				
				
				if (min == null && max == null) {
					continue;
				}
				
				int avg = Math.round((min + max) / 2);
				
				if (!queryString.isEmpty()) {
					queryString += " AND ";
				}
				queryString += " property" + i + "_min <= " + avg + " AND property" + i + "_max >=" + avg;

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			
		}
		return space.takeMultiple(new SQLQuery<DoubleAuctionTemplate>(DoubleAuctionTemplate.class, queryString), Integer.MAX_VALUE);
	}

	public void writePriceTemplate(PriceTemplate priceTemplate) {
		space.write(priceTemplate);
	}	
	
}
