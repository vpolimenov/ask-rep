package ask_rep.server;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

import ask_rep.client.SearchService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class SearchServiceImpl extends RemoteServiceServlet implements SearchService{

    private final String searchEngine = "https://www.googleapis.com/customsearch/v1?";
    private final String API_Key =  "AIzaSyC8li-6nHMesNzk9gxeeLIc0_WM-8oIzFY";
    private final String cx_Key = "014207427954762379102:cj7uilhje_0";
    //011528944153720704846:3Aqrk4wd5jqfu
    private final String outputFormat = "json";

    private String queryString = "Http java request";

    public SearchServiceImpl(String queryString) {
        this.queryString = queryString;
    }

    public SearchServiceImpl() {

    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public ArrayList<String> getSearchResults() throws IOException {
        if (queryString == "") throw new IllegalStateException("Must set query string");

        ArrayList<String> urlResultList = new ArrayList<>();
        queryString = queryString.replaceAll(" ", "+");

        URL searchURL = new URL(searchEngine +
                "key=" + API_Key +
                "&cx=" + cx_Key +
                "&q=" + queryString +
                "&alt=" + outputFormat);
        URLConnection searchResult = searchURL.openConnection();

        Scanner input = new Scanner(searchResult.getInputStream());
        String inputLine;
        int index;
        while (input.hasNextLine() && urlResultList.size() < 10) {
            inputLine = input.nextLine();
            index = inputLine.indexOf("\"link\"");
            if (index != -1) {
                urlResultList.add(inputLine.substring(index + 9, inputLine.length() - 2));
            }
        }
        input.close();

        return urlResultList;
    }
}
