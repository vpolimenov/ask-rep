package ask_rep.server;

import ask_rep.client.SessionService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class SessionServiceImpl extends RemoteServiceServlet implements SessionService {
	
	public void createSession(String Name, String Value) {
		if (getThreadLocalRequest() != null) {		
	        if(!validateSession(Name)) {
	        	getThreadLocalRequest().getSession().setAttribute(Name, Value);
	        }
		}
    }

	public boolean validateSession(String Name) {
		if (getThreadLocalRequest() != null) {		
	        if (getThreadLocalRequest().getSession().getAttribute(Name) != null) {
	            return true;
	        } 
		}
		return false;
    }
	
	public void invalidateSession(String Name) {
		if(validateSession(Name)) {
			getThreadLocalRequest().getSession().invalidate();
		}
	}

}
