package slaAuctions.server;

import java.rmi.RemoteException;

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
		Template tpl = new Template();
		tpl.setPrice(1000);
		space.write(tpl);
		tpl = new Template();
		tpl.setPrice(8000);
		space.write(tpl);
		System.out.println(space);
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
					System.out.println("Element with price " + tpl.getPrice()
							+ " got taken!");
				} catch (UnusableEntryException e) {
					e.printStackTrace();
				}
			}
		}, Lease.FOREVER, null, null, NotifyActionType.NOTIFY_TAKE);

		System.out.println("READY");
	}
	
}
