package slaAuctions.entities;

import com.gigaspaces.annotation.pojo.SpaceClass;

@SpaceClass
public class Match {
	private Integer providerId;

	public Integer getProviderId() {
		return providerId;
	}

	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}
	
}
