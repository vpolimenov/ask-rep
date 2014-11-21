package ask_rep.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SearchServiceAsync {

	public void setQueryString(String queryString, AsyncCallback<Void> callback);
	public void getSearchResults(AsyncCallback<ArrayList<String>> callback);

}
