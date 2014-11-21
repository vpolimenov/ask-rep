package ask_rep.server;

import ask_rep.client.LoginInfo;
import ask_rep.client.LoginService;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService{
	
	public LoginInfo login(String requestUri) {
		UserService objUserService = UserServiceFactory.getUserService();
		
		User objUser = objUserService.getCurrentUser();
		LoginInfo objLoginInfo = new LoginInfo();
		
		if (objUser != null) {	
			objLoginInfo.setLoggedIn(true);
			objLoginInfo.setEmailAddress(objUser.getEmail());
			objLoginInfo.setNickname(objUser.getNickname());
			objLoginInfo.setLogoutUrl(objUserService.createLogoutURL(requestUri));  
	    } else { 	
	    	objLoginInfo.setLoggedIn(false);
	    	objLoginInfo.setLoginUrl(objUserService.createLoginURL(requestUri));  
	    }
		
		
	    return objLoginInfo;
	}
}
