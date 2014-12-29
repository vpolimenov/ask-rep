package ask_rep.client;

import java.io.FileInputStream;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface FileServiceAsync {
	public void getFiles(int repositoryID, int folderID, AsyncCallback<List<FileInfo>> asyncCallback);
	public void insertFile(String filename, String extension, String fileContent, int folderID, int repositoryID, AsyncCallback<Integer> asyncCallback);
	public void getFile(int fileID, AsyncCallback<FileInfo> asyncCallback);
}
