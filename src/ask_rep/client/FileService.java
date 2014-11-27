package ask_rep.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.ui.VerticalPanel;

@RemoteServiceRelativePath("file")
public interface FileService extends RemoteService {
	public List<FileInfo> getFiles(int repositoryID, int folderID);
}
