package ask_rep.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService {
	public int insertUser(String Name, String Email);
	public int checkUserExists(String Email);
}
