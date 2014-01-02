package slaAuctions.providerBeans;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;

public class AuctioneerBean {

	@GigaSpaceContext
	private GigaSpace space;
	
	public void makePrice() {
		System.out.println("Waiting for price...");
	}
	
}
