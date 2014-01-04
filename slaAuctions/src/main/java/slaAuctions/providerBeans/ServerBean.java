package slaAuctions.providerBeans;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lease.Lease;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;

import slaAuctions.entities.Match;

import com.gigaspaces.events.DataEventSession;
import com.gigaspaces.events.EventSessionFactory;
import com.gigaspaces.events.NotifyActionType;
import com.j_spaces.core.client.EntryArrivedRemoteEvent;

public class ServerBean {
	@GigaSpaceContext
	private GigaSpace space;
	
	private List<Match> matches = new ArrayList<Match>();
	
	/**
	 * Register for take events on the space (will be used for matching later)
	 * @throws Exception
	 */
	public void doNotify() throws Exception {
		EventSessionFactory factory = EventSessionFactory.getFactory(space
				.getSpace());
		DataEventSession session = factory.newDataEventSession();
		session.addListener(new Match(), new RemoteEventListener() {
			public void notify(RemoteEvent event) throws UnknownEventException,
					RemoteException {
				EntryArrivedRemoteEvent arrivedRemoteEvent = (EntryArrivedRemoteEvent) event;
				try {
					Match match = (Match) arrivedRemoteEvent.getObject();
					System.out.println("Got match for provider " + match.getProviderId() + " and customer " + match.getCustomerId());
					matches.add(match);
				} catch (UnusableEntryException e) {
					e.printStackTrace();
				}
			}
		}, Lease.FOREVER, null, null, NotifyActionType.NOTIFY_TAKE);

		System.out.println("READY");
	}
	
	public List<Match> getMatches() {
		return matches;
	}
}
