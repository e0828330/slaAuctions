package slaAuctions.customerBeans;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import slaAuctions.entities.Template;

import com.j_spaces.core.client.SQLQuery;

@Transactional
public class TestBean {
	@GigaSpaceContext
	private GigaSpace space;
	
	@Transactional (propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	/**
	 * Transaction test / example
	 * @param error
	 * @throws Exception
	 */
	public void take(boolean error) throws Exception {
		Template result = space.take(new SQLQuery<Template>(Template.class, "currentValues.price < ?", 2000), Integer.MAX_VALUE);
		System.out.println(result.getCurrentValues().get("price"));
		if (error) {
			throw new Exception("WTF");
		}
	}
	
}
