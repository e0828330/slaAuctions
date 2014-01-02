package slaAuctions.providerBeans;

import org.apache.log4j.Logger;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.transaction.annotation.Transactional;

import slaAuctions.entities.DoubleAuctionTemplate;

@Transactional
public class AuctioneerBean {

	@GigaSpaceContext
	private GigaSpace space;
	
	Logger logger = Logger.getLogger(getClass());
	
	public void receive() {
		int totalNumber = space.count(new DoubleAuctionTemplate());
		System.out.println(totalNumber + " of DoubleAuctionTemplates are in the space.");
		
		
		//space.take(new DoubleAuctionTemplate(), Integer.MAX_VALUE);
	}
	
}
