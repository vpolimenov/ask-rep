package ask_rep.client;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("search")
public interface SearchService extends RemoteService{

	public void setQueryString(String queryString);
	public ArrayList<String> getSearchResults() throws IOException;
}
