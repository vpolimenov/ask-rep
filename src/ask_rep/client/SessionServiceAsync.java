package ask_rep.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SessionServiceAsync {
	public void createSession(String Name, String Value, AsyncCallback<Void> callback);
	public void validateSession(String Name, AsyncCallback<Boolean> callback);
	public void invalidateSession(String Name, AsyncCallback<Void> callback);
}