package slaAuctions.providerBeans;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.transaction.annotation.Transactional;

import com.j_spaces.core.client.SQLQuery;

import slaAuctions.entities.DoubleAuctionTemplate;
import slaAuctions.entities.Match;
import slaAuctions.entities.PriceTemplate;

@Transactional
public class DoubleProviderBean {
	@GigaSpaceContext
	private GigaSpace space;

	public void writeAuctioneerTemplate(DoubleAuctionTemplate tpl) {
		space.write(tpl);
	}

	public PriceTemplate waitForTemplate(String providerId) {
		String queryString = "providerId = '" + providerId + "'";
		return space.take(new SQLQuery<PriceTemplate>(PriceTemplate.class, queryString), Integer.MAX_VALUE);
	}
	
	public boolean waitForMatch(String providerId, Integer timeout) {
		Match result = space.take(new SQLQuery<Match>(Match.class, "providerId = ?", providerId), timeout);
		return result != null;
	}	

}
