package slaAuctions.customerBeans;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DoubleCustomerBean {
	@GigaSpaceContext
	private GigaSpace doubleAuctionSpace;

}
