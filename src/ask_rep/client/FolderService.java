package ask_rep.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("folder")
public interface FolderService extends RemoteService {
	public List<FolderInfo> getFolders(int repositoryID, int parentFolderID);
	public FolderInfo getFolder(int FolderID);
	public int insertFolder(String Name, int ParentFolderID, int repositoryID);
}
