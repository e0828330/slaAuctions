package slaAuctions.entities;

import com.gigaspaces.annotation.pojo.SpaceClass;


@SpaceClass
public class Template {
	public Template() { }
	
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	private Integer price;
	
}
