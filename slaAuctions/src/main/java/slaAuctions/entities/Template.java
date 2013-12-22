package slaAuctions.entities;

import java.util.HashMap;
import java.util.Map;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

@SpaceClass
public class Template {
	private String uid;

	private Integer providerId;
	
	/* Maps for the properties, have min, max and current values */
	private Map<String, Integer> minValues;
	private Map<String, Integer> maxValues;
	private Map<String, Integer> currentValues;
	
	/* Default constructor is required */
	public Template() { }
	
	
	public Template(Map<String, Integer> minValues, Map<String, Integer> currentValues, Map<String, Integer> maxValues) {
		this.minValues = new HashMap<String, Integer>(minValues);
		this.currentValues = new HashMap<String, Integer>(currentValues);
		this.maxValues = new HashMap<String, Integer>(maxValues);
	}

	@SpaceId (autoGenerate=true)
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Map<String, Integer> getMinValues() {
		return minValues;
	}

	public void setMinValues(Map<String, Integer> minValues) {
		this.minValues = minValues;
	}

	public Map<String, Integer> getMaxValues() {
		return maxValues;
	}

	public void setMaxValues(Map<String, Integer> maxValues) {
		this.maxValues = maxValues;
	}

	public Map<String, Integer> getCurrentValues() {
		return currentValues;
	}

	public void setCurrentValues(Map<String, Integer> currentValues) {
		this.currentValues = currentValues;
	}


	public Integer getProviderId() {
		return providerId;
	}


	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}
}
