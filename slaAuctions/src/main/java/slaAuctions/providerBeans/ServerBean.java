package slaAuctions.providerBeans;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lease.Lease;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.transaction.annotation.Transactional;

import com.gigaspaces.events.DataEventSession;
import com.gigaspaces.events.EventSessionFactory;
import com.gigaspaces.events.NotifyActionType;
import com.j_spaces.core.client.EntryArrivedRemoteEvent;

import slaAuctions.entities.Template;

public class ServerBean {
	@GigaSpaceContext
	private GigaSpace space;
	
	@Transactional
	/**
	 * Write 2 templates into the space
	 */
	public void write() {
		Map<String, Integer> values = new HashMap<String, Integer>();
		values.put("price", 1000);
		Template tpl = new Template();
		tpl.setCurrentValues(values);
		space.write(tpl);
		
		values = new HashMap<String, Integer>();
		tpl = new Template();
		values.put("price", 8000);
		tpl.setCurrentValues(values);
		space.write(tpl);
	}
	
	/**
	 * Register for take events on the space (will be used for matching later)
	 * @throws Exception
	 */
	public void doNotify() throws Exception {
		EventSessionFactory factory = EventSessionFactory.getFactory(space
				.getSpace());
		DataEventSession session = factory.newDataEventSession();
		session.addListener(new Template(), new RemoteEventListener() {
			public void notify(RemoteEvent event) throws UnknownEventException,
					RemoteException {
				EntryArrivedRemoteEvent arrivedRemoteEvent = (EntryArrivedRemoteEvent) event;
				try {
					Template tpl = (Template) arrivedRemoteEvent.getObject();
					System.out.println("Element with price " + tpl.getCurrentValues().get("price")
							+ " got taken!");
				} catch (UnusableEntryException e) {
					e.printStackTrace();
				}
			}
		}, Lease.FOREVER, null, null, NotifyActionType.NOTIFY_TAKE);

		System.out.println("READY");
	}
	
}
