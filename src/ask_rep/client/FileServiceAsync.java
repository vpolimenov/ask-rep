package ask_rep.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface FileServiceAsync {
	public void getFiles(int repositoryID, int folderID, AsyncCallback<List<FileInfo>> asyncCallback);
}
