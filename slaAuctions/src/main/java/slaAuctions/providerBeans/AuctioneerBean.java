package slaAuctions.providerBeans;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.print.attribute.standard.DateTimeAtCompleted;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.transaction.annotation.Transactional;

import com.j_spaces.core.client.SQLQuery;

import slaAuctions.entities.DoubleAuctionTemplate;
import slaAuctions.entities.Template;

@Transactional
public class AuctioneerBean {

	@GigaSpaceContext
	private GigaSpace space;
	
	private int TIMEOUT = 60 * 2 * 1000; // 1 minute hardcoded for now
	
	private ConcurrentHashMap<Integer, ArrayList<DoubleAuctionTemplate>> groups;
	
	private int groupNumber = 0;
	
	Logger logger = Logger.getLogger(getClass());
	
	public void receive() {
		groups = new ConcurrentHashMap<Integer, ArrayList<DoubleAuctionTemplate>>();
		
		int totalTemplateNumber = space.count(new DoubleAuctionTemplate());
		
		System.out.println(totalTemplateNumber + " of DoubleAuctionTemplates are in the space.");
		
		Date start = new Date();
		
		while (totalTemplateNumber > 0) {
			// If total timeout is reached, leave
			if (start.getTime() + TIMEOUT < (new Date()).getTime()) {
				break;
			}
			
			// Take some template
			DoubleAuctionTemplate match = space.take(new DoubleAuctionTemplate(), Integer.MAX_VALUE);
			totalTemplateNumber--;
			
			// Receive all templates with same properties
			ArrayList<DoubleAuctionTemplate> templates = new ArrayList<DoubleAuctionTemplate>();
			templates.add(match);
		
			// Query all templates which refer to 'match'
			DoubleAuctionTemplate[] same = this.receiveSameTemplates(match);
			for (DoubleAuctionTemplate temp : same) {
				templates.add(temp);
			}
			totalTemplateNumber -= same.length;
			
			this.groups.put(new Integer(groupNumber++), templates);
		}
		
		System.out.println("Created " + this.groups.size() + " groups.");
		for (Entry<Integer, ArrayList<DoubleAuctionTemplate>> entry : this.groups.entrySet()) {
			
			
			int price = this.calculatePrice(entry.getValue());
			
			System.out.println("Group Nr.: " + entry.getKey() +" gets price: " + price);
			
			for (DoubleAuctionTemplate t : entry.getValue()) {
				t.setPrice(new Integer(price));
			}
		}
		
	}
	
	// Average price
	private int calculatePrice(ArrayList<DoubleAuctionTemplate> templates) {
		int price = 0;
		for (DoubleAuctionTemplate template : templates) {
			if (template.getCustomerId() != null) {
				if (template.getPrice_max() != null) {
					price += template.getPrice_max();
				}
			}
			else {
				if (template.getPrice_max() != null && template.getPrice_min() != null) {
					price += Math.round((template.getPrice_max() + template.getPrice_min()) / 2);
				}
			}
		}
		return Math.round(price / templates.size());
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
	
}
