package ask_rep.shared;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class SnippetContainer implements Serializable {

	private ArrayList<Snippet> snippets;

    public SnippetContainer() {
        snippets = new ArrayList<>();
    }

    public ArrayList<Snippet> getSnippets() {
        return snippets;
    }

    public void setSnippets(ArrayList<Snippet> snippets) {
        this.snippets = snippets;
    }

    public void addSnippet(Snippet snippet) {
        snippets.add(snippet);
    }
}
