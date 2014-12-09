package ask_rep.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FolderServiceAsync {
	public void getFolders(int repositoryID, int parentFolderID, AsyncCallback<List<FolderInfo>> asyncCallback);
	public void getFolder(int folderID, AsyncCallback<FolderInfo> asyncCallback);
	public void insertFolder(String Name, int ParentFolderID, int repositoryID, AsyncCallback<Integer> asyncCallback);
}
