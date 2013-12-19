package slaAuctions.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import slaAuctions.utils.DataGridConnectionUtility;

public class Test {
	public static void main(String[] args) throws Exception {
		/* Create the space if it does not exist */
		DataGridConnectionUtility.getSpace("auctionSpace");
		
		/* Create app context */
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("file:src/main/resources/Application.xml");
		
		TestBean bean = (TestBean) context.getBean("testBean");
		
		/* With rollback */
		try {
			bean.take(true);
		}
		catch (Exception e) {
			
		}
		/* No rollback */
		bean.take(false);
		
	}
}
