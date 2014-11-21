package ask_rep.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("session")
public interface SessionService extends RemoteService {
	public void createSession(String Name, String Value);
	public boolean validateSession(String Name);
	public void invalidateSession(String Name);
}
