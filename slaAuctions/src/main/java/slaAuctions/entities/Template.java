package slaAuctions.entities;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

@SpaceClass
public class Template {
	private String uid;

	private String providerId;
	
	private String customerId;
	
	/* Default constructor is required */
	public Template() { }
	
	private Integer price_min;
	private Integer price;
	private Integer price_max;

	private Integer property0_min;
	private Integer property0_current;
	private Integer property0_max;

	private Integer property1_min;
	private Integer property1_current;
	private Integer property1_max;

	private Integer property2_min;
	private Integer property2_current;
	private Integer property2_max;
	
	private Integer property3_min;
	private Integer property3_current;
	private Integer property3_max;
	
	private Integer property4_min;
	private Integer property4_current;
	private Integer property4_max;
	
	private Integer property5_min;
	private Integer property5_current;
	private Integer property5_max;
	
	private Integer property6_min;
	private Integer property6_current;
	private Integer property6_max;
	
	private Integer property7_min;
	private Integer property7_current;
	private Integer property7_max;
	
	private Integer property8_min;
	private Integer property8_current;
	private Integer property8_max;

	public Integer getPrice() {
		return price;
	}

	public Integer getPrice_min() {
		return price_min;
	}

	public void setPrice_min(Integer price_min) {
		this.price_min = price_min;
	}

	public Integer getPrice_max() {
		return price_max;
	}

	public void setPrice_max(Integer price_max) {
		this.price_max = price_max;
	}
	
	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getProperty0_min() {
		return property0_min;
	}

	public void setProperty0_min(Integer property0_min) {
		this.property0_min = property0_min;
	}

	public Integer getProperty0_current() {
		return property0_current;
	}

	public void setProperty0_current(Integer property0_current) {
		this.property0_current = property0_current;
	}

	public Integer getProperty0_max() {
		return property0_max;
	}

	public void setProperty0_max(Integer property0_max) {
		this.property0_max = property0_max;
	}

	public Integer getProperty1_min() {
		return property1_min;
	}

	public void setProperty1_min(Integer property1_min) {
		this.property1_min = property1_min;
	}

	public Integer getProperty1_current() {
		return property1_current;
	}

	public void setProperty1_current(Integer property1_current) {
		this.property1_current = property1_current;
	}

	public Integer getProperty1_max() {
		return property1_max;
	}

	public void setProperty1_max(Integer property1_max) {
		this.property1_max = property1_max;
	}

	public Integer getProperty2_min() {
		return property2_min;
	}

	public void setProperty2_min(Integer property2_min) {
		this.property2_min = property2_min;
	}

	public Integer getProperty2_current() {
		return property2_current;
	}

	public void setProperty2_current(Integer property2_current) {
		this.property2_current = property2_current;
	}

	public Integer getProperty2_max() {
		return property2_max;
	}

	public void setProperty2_max(Integer property2_max) {
		this.property2_max = property2_max;
	}

	public Integer getProperty3_min() {
		return property3_min;
	}

	public void setProperty3_min(Integer property3_min) {
		this.property3_min = property3_min;
	}

	public Integer getProperty3_current() {
		return property3_current;
	}

	public void setProperty3_current(Integer property3_current) {
		this.property3_current = property3_current;
	}

	public Integer getProperty3_max() {
		return property3_max;
	}

	public void setProperty3_max(Integer property3_max) {
		this.property3_max = property3_max;
	}

	public Integer getProperty4_min() {
		return property4_min;
	}

	public void setProperty4_min(Integer property4_min) {
		this.property4_min = property4_min;
	}

	public Integer getProperty4_current() {
		return property4_current;
	}

	public void setProperty4_current(Integer property4_current) {
		this.property4_current = property4_current;
	}

	public Integer getProperty4_max() {
		return property4_max;
	}

	public void setProperty4_max(Integer property4_max) {
		this.property4_max = property4_max;
	}

	public Integer getProperty5_min() {
		return property5_min;
	}

	public void setProperty5_min(Integer property5_min) {
		this.property5_min = property5_min;
	}

	public Integer getProperty5_current() {
		return property5_current;
	}

	public void setProperty5_current(Integer property5_current) {
		this.property5_current = property5_current;
	}

	public Integer getProperty5_max() {
		return property5_max;
	}

	public void setProperty5_max(Integer property5_max) {
		this.property5_max = property5_max;
	}

	public Integer getProperty6_min() {
		return property6_min;
	}

	public void setProperty6_min(Integer property6_min) {
		this.property6_min = property6_min;
	}

	public Integer getProperty6_current() {
		return property6_current;
	}

	public void setProperty6_current(Integer property6_current) {
		this.property6_current = property6_current;
	}

	public Integer getProperty6_max() {
		return property6_max;
	}

	public void setProperty6_max(Integer property6_max) {
		this.property6_max = property6_max;
	}

	public Integer getProperty7_min() {
		return property7_min;
	}

	public void setProperty7_min(Integer property7_min) {
		this.property7_min = property7_min;
	}

	public Integer getProperty7_current() {
		return property7_current;
	}

	public void setProperty7_current(Integer property7_current) {
		this.property7_current = property7_current;
	}

	public Integer getProperty7_max() {
		return property7_max;
	}

	public void setProperty7_max(Integer property7_max) {
		this.property7_max = property7_max;
	}

	public Integer getProperty8_min() {
		return property8_min;
	}

	public void setProperty8_min(Integer property8_min) {
		this.property8_min = property8_min;
	}

	public Integer getProperty8_current() {
		return property8_current;
	}

	public void setProperty8_current(Integer property8_current) {
		this.property8_current = property8_current;
	}

	public Integer getProperty8_max() {
		return property8_max;
	}

	public void setProperty8_max(Integer property8_max) {
		this.property8_max = property8_max;
	}

	@SpaceId (autoGenerate=true)
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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
