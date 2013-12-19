package slaAuctions.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import slaAuctions.utils.DataGridConnectionUtility;

public class Main {

	public static void main(String[] args) throws Exception {
		/* Create the space if it does not exist */
		DataGridConnectionUtility.getSpace("auctionSpace");
		/* Create app context */
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("file:src/main/resources/Application.xml");
		
		ServerBean bean = (ServerBean) context.getBean("serverBean");

		bean.write();
		bean.doNotify();
	}

}
