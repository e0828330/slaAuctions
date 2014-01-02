package slaAuctions.customerBeans;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.transaction.annotation.Transactional;

import com.j_spaces.core.client.SQLQuery;

import slaAuctions.entities.DoubleAuctionTemplate;
import slaAuctions.entities.PriceTemplate;

@Transactional
public class DoubleCustomerBean {
	@GigaSpaceContext
	private GigaSpace space;
	
	public void writeAuctioneerTemplate(DoubleAuctionTemplate tpl) {
		space.write(tpl);
	}

	public PriceTemplate waitForTemplate(Integer customerId) {
		String queryString = "customerId = " + customerId;
		return space.take(new SQLQuery<PriceTemplate>(PriceTemplate.class, queryString), Integer.MAX_VALUE);
	}

}
