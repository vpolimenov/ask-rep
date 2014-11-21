package ask_rep.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ask_rep.client.CodeExtractionService;
import ask_rep.shared.Snippet;
import ask_rep.shared.SnippetRater;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class CodeExtractionServiceImpl extends RemoteServiceServlet implements CodeExtractionService{ 

    
    HashMap<String, Elements> codeTags;
    private ArrayList<Snippet> snippets;

    SnippetRater rater = new SnippetRater();

    public CodeExtractionServiceImpl() {
        codeTags = new HashMap<>();
        snippets = new ArrayList<>();
        SnippetRater.initialise();
    }

    public void extractCodeTags(ArrayList<String> resultLinks) { //, AnswerPanel answers
        codeTags.clear();
        if (resultLinks == null) throw new IllegalStateException("Links not sent");
        for (String link : resultLinks) {
            try {
                Document codePage = Jsoup.connect(link).get();
                codeTags.put(link, codePage.getElementsByTag("code"));
            } catch (IOException e) {
//                answers.printError("JSOUP Error, page issue @: " + link);
            	System.out.println("Error: JSoup! CodeExtractionServiceImpl");
            }
        }
    }

    public void extractSnippets(String searchLanguage, String keyWords, int min, int max) {
        snippets.clear();

        for (String url : codeTags.keySet()) {
            Elements elements = codeTags.get(url);
            for (Element codeElement : elements) {
                String snippet = codeElement.text();
                if(snippet.length() < min || snippet.length() > max)
                	continue;
                snippets.add(rater.rateSnippet(new Snippet(keyWords, url, snippet, searchLanguage)));
                snippets.add(new Snippet(keyWords, url, snippet, searchLanguage));
            }
        }
        Collections.sort(snippets);
    }

    public ArrayList<Snippet> getSnippets() {
        return snippets;
    }

    public void saveCodeTags(String filename) throws IOException {
//
//        File file = new File(filename);
//        if (!file.exists()) {
//            // Result is whether it already existed, we know this already
//            //noinspection ResultOfMethodCallIgnored
//            file.createNewFile();
//        }
//
//        BufferedWriter bufferedWriterToFile = new BufferedWriter(new FileWriter(file));
//        bufferedWriterToFile.write("<html><head><title>Code Snippets</title></head><body>");
//
//        int codeTagCount = 0;
//        for (Snippet snippet : snippets) {
//            codeTagCount++;
//            bufferedWriterToFile.write("<p>CODE SnippetServer.Snippet: " + codeTagCount + "</br><pre><code>" + snippet + "</code></pre></br></br></p>");
//        }
//        bufferedWriterToFile.write("</body></html>");
//        bufferedWriterToFile.close();
    }
}
