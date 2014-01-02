package slaAuctions.providerBeans;

import org.apache.log4j.Logger;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;

import slaAuctions.entities.DoubleAuctionTemplate;

public class AuctioneerBean {

	@GigaSpaceContext
	private GigaSpace space;
	
	Logger logger = Logger.getLogger(getClass());
	
	public void makePrice() {
		logger.info("Wait for new auctioneer template");
		space.read(new DoubleAuctionTemplate(), Integer.MAX_VALUE);
		System.out.println("RECEIVED");
	}
	
}
