package ask_rep.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Snippet implements Comparable<Snippet>, Serializable {
    private String text;
    private int rating = 0;
    private String language = "Unknown";
    private String searchLanguage;

	private String url;

    private boolean recursive = false;
    private String keywords;


    @Deprecated
    public Snippet(String text, String url) {
        this.text = text;
        this.url = url;
    }

    // Much better to keep the keywords with the Snippet for later indexing
    public Snippet(String keywords, String url, String text) {
    	this.keywords = keywords;
        this.url = url;
        this.text = text;
    }
    
    public Snippet(String keywords, String url, String text, String searchLanguage) {
    	this.keywords = keywords;
        this.url = url;
        this.text = text;
        this.searchLanguage = searchLanguage;
    }

    public Snippet() {

    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSearchLanguage() {
		return searchLanguage;
	}

	public void setSearchLanguage(String searchLanguage) {
		this.searchLanguage = searchLanguage;
	}

    public int length() {
        return text.length();
    }

    @Override
    public int compareTo(Snippet s) {
        return s.rating - rating;
    }

    @Override
    public String toString() {
        return text;
    }

}
