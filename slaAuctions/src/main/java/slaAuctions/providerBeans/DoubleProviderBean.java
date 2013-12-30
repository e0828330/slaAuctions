package slaAuctions.providerBeans;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DoubleProviderBean {
	@GigaSpaceContext
	private GigaSpace doubleAuctionSpace;

}
