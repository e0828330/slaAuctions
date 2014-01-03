package slaAuctions.agents;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.springframework.context.ApplicationContext;

import slaAuctions.entities.DoubleAuctionTemplate;
import slaAuctions.entities.PriceTemplate;
import slaAuctions.entities.Template;
import slaAuctions.providerBeans.AuctioneerBean;

public class Auctioneer extends Agent {

	private int ROUNDTIME = 5000;
	
	private CountDownLatch latch;
	
	public Auctioneer(ApplicationContext context, Template template, CountDownLatch latch) {
		super(context, template);
		this.bean = (AuctioneerBean) context.getBean("auctioneerBean");
		this.latch = latch;
	}
	
	private AuctioneerBean bean;
	
	private ConcurrentHashMap<Integer, ArrayList<DoubleAuctionTemplate>> groups;
	
	private int groupNumber = 0;
	
	private int TIMEOUT = 60 * 2 * 1000; // 2 minutes hardcoded for now

	public void run() {
		try {
			System.out.println("Auctioneer blocking...");
			latch.await();
			System.out.println("Auctioneer has finished blocking...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int totalTemplateNumber = bean.getTotalNumber();
		System.out.println(totalTemplateNumber + " of DoubleAuctionTemplates are in the space.");
		
		groups = new ConcurrentHashMap<Integer, ArrayList<DoubleAuctionTemplate>>();
		
		Date start = new Date();
		
		while (totalTemplateNumber > 0) {
			// If total timeout is reached, leave
			if (start.getTime() + TIMEOUT < (new Date()).getTime()) {
				break;
			}
			
			// Take some template
			DoubleAuctionTemplate match = bean.getDoubleAucationTemplate();
			totalTemplateNumber--;
			
			// Receive all templates with same properties
			ArrayList<DoubleAuctionTemplate> templates = new ArrayList<DoubleAuctionTemplate>();
			templates.add(match);
		
			// Query all templates which refer to 'match'
			DoubleAuctionTemplate[] same = bean.receiveSameTemplates(match);
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
			
			// set the new price
			for (DoubleAuctionTemplate t : entry.getValue()) {
				t.setPrice(new Integer(price));
			}
			
			
			for (DoubleAuctionTemplate t : entry.getValue()) {
				// All the sellers who asked less than p sell
				if (t.getProviderId() != null && Math.round((t.getPrice_max() + t.getPrice_min()) / 2) <= price) {
					System.out.println("Write template with fixed price");
					bean.writePriceTemplate(new PriceTemplate(t));
					bean.writeTemplate((Template) t);
				}
				// and all buyers who bid more than p buy
				else if (t.getCustomerId() != null && t.getPrice_max() >= price) {
					System.out.println("Write template with fixed price");
					bean.writePriceTemplate(new PriceTemplate(t));
					bean.writeTemplate((Template) t);
				}
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

}
