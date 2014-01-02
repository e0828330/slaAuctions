package slaAuctions.customerBeans;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.transaction.annotation.Transactional;

import slaAuctions.entities.DoubleAuctionTemplate;

@Transactional
public class DoubleCustomerBean {
	@GigaSpaceContext
	private GigaSpace space;
	
	public void writeAuctioneerTemplate(DoubleAuctionTemplate tpl) {
		space.write(tpl);
	}

}
