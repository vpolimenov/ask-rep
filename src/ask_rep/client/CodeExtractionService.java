package ask_rep.client;

import java.io.IOException;
import java.util.ArrayList;

import ask_rep.shared.Snippet;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("codeExtract")
public interface CodeExtractionService  extends RemoteService{

	public void extractCodeTags(ArrayList<String> resultLinks); //, AnswerPanel answers
	public void  extractSnippets(String searchLanguage, String keyWords, int min, int max);
	public ArrayList<Snippet> getSnippets();
	public void saveCodeTags(String filename) throws IOException;
}
