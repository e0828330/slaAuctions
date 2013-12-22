package slaAuctions.entities;

import com.gigaspaces.annotation.pojo.SpaceClass;

@SpaceClass
public class Match {
	private Integer providerId;

	public Match() { }
	
	public Match(Integer providerId) {
		this.providerId = providerId;
	}
	
	public Integer getProviderId() {
		return providerId;
	}

	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}
	
}
