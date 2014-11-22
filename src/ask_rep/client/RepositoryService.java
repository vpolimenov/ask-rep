package ask_rep.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("repository")
public interface RepositoryService extends RemoteService {
	public RepositoryInfo insertRepository(String Name, int UserID);
}
