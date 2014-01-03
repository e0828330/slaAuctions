package slaAuctions.entities;

import com.gigaspaces.annotation.pojo.SpaceClass;


/**
 * Just used to have a different type for double auctions in the space
 */
@SpaceClass
public class PriceTemplate extends Template {
	public PriceTemplate() { }
    
    public PriceTemplate(Template tpl) {

    		this.setProviderId(tpl.getProviderId());
    		this.setCustomerId(tpl.getCustomerId());
    	
            this.setPrice(tpl.getPrice());
            this.setPrice_max(tpl.getPrice_max());
            this.setPrice_min(tpl.getPrice_min());
   
            this.setProperty0_current(tpl.getProperty0_current());
            this.setProperty0_min(tpl.getProperty0_min());
            this.setProperty0_max(tpl.getProperty0_max());
           
            this.setProperty1_current(tpl.getProperty1_current());
            this.setProperty1_min(tpl.getProperty1_min());
            this.setProperty1_max(tpl.getProperty1_max());
           
            this.setProperty2_current(tpl.getProperty2_current());
            this.setProperty2_min(tpl.getProperty2_min());
            this.setProperty2_max(tpl.getProperty2_max());
           
            this.setProperty3_current(tpl.getProperty3_current());
            this.setProperty3_min(tpl.getProperty3_min());
            this.setProperty3_max(tpl.getProperty3_max());
            
            this.setProperty4_current(tpl.getProperty4_current());
            this.setProperty4_min(tpl.getProperty4_min());
            this.setProperty4_max(tpl.getProperty4_max());
           
            this.setProperty5_current(tpl.getProperty5_current());
            this.setProperty5_min(tpl.getProperty5_min());
            this.setProperty5_max(tpl.getProperty5_max());
            
            this.setProperty6_current(tpl.getProperty6_current());
            this.setProperty6_min(tpl.getProperty6_min());
            this.setProperty6_max(tpl.getProperty6_max());
           
            this.setProperty7_current(tpl.getProperty7_current());
            this.setProperty7_min(tpl.getProperty7_min());
            this.setProperty7_max(tpl.getProperty7_max());
            
            this.setProperty8_current(tpl.getProperty8_current());
            this.setProperty8_min(tpl.getProperty8_min());
            this.setProperty8_max(tpl.getProperty8_max());    
    }
}
