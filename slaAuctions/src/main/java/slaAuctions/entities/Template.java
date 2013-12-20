package slaAuctions.entities;

import java.util.Map;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;


@SpaceClass
public class Template {
	public Template() { }

	private String uid;

	/* Maps for the properties, have min, max and current values */
	private Map<String, Integer> minValues;
	private Map<String, Integer> maxValues;
	private Map<String, Integer> currentValues;
	
	public String getUid() {
		return uid;
	}

	@SpaceId (autoGenerate=true)
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
}
