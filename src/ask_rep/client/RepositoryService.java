package ask_rep.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("repository")
public interface RepositoryService extends RemoteService {
	public int insertRepository(String Name, int UserID);
	public RepositoryInfo getRepository(int RepositoryID);
	public List<RepositoryInfo> getRepositoryByUserID(int UserID);
	public List<RepositoryInfo> getLatestRepositories(int UserID);
}
