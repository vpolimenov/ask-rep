package ask_rep.client;

import java.util.ArrayList;

import ask_rep.shared.Snippet;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CodeExtractionServiceAsync {

	public void extractCodeTags(ArrayList<String> resultLinks, AsyncCallback<Void> callback); //, AnswerPanel answers
	public void extractSnippets(String searchLanguage, String keyWords, int min, int max, AsyncCallback<Void> callback);
	public void getSnippets(AsyncCallback<ArrayList<Snippet>> callback);
	public void saveCodeTags(String filename, AsyncCallback<Void> callback);

}
