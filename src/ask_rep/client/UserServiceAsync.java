package ask_rep.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {
  public void insertUser(String Name, String Email, AsyncCallback<Integer> asyncCallBack);
  public void checkUserExists(String Email, AsyncCallback<Integer> asyncCallback);
}