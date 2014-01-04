package slaAuctions.entities;

import com.gigaspaces.annotation.pojo.SpaceClass;

@SpaceClass
public class Match {
	private String providerId;
	private String customerId;

	public Match() { }
	
	public Match(String providerId, String customerId) {
		this.providerId = providerId;
		this.customerId = customerId;
	}
	
	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
}
