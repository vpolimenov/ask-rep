package ask_rep.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RepositoryServiceAsync {
  public void insertRepository(String Name, int UserID, AsyncCallback<Integer> asyncCallback);
  public void getRepository(int RepositoryID, AsyncCallback<RepositoryInfo> asyncCallback);
}