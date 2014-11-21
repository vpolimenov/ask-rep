package ask_rep.shared;

import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("serial")
public class SnippetRater implements Serializable {

    public static String[] javaCodeTags, cppCodeTags, pythonCodeTags, csharpCodeTags;

    public static HashMap<String, String[]> codeTags;

    private Snippet currentSnippet;

    public Snippet rateSnippet(Snippet input) {
        currentSnippet = input;

        currentSnippet.setLanguage(getLanguage());
        currentSnippet.setRecursive(getRecursive());

        currentSnippet.setRating(getRating());

        return currentSnippet;
    }

    public static String[] commonCodeTags = new String[]{"public ","protected ","private ","static ","main","int ","if","else","for","while","case ","return ","string ","class "};
    
    protected int getRating() {
        //TODO base rating function on metrics design
        int rating = 0;

        if (!currentSnippet.getLanguage().equals(getSearchLanguage())) {
        	rating -= 20;
        	System.out.println(currentSnippet.getText());
        	System.out.println(currentSnippet.getLanguage() + " vs " + getSearchLanguage());
        }

        int occurs = getOccurances(currentSnippet.getKeywords(), currentSnippet.getText());
        rating += occurs>0? 10 : 0;
        System.out.print(rating+" ");
        
        for (String s : currentSnippet.getKeywords().split(" ")) {
            rating += getOccurances(s, currentSnippet.getText())>0 ? 5 : 0;
        }
        System.out.print(rating+" ");
        
        String[] wordsOnly = currentSnippet.getText().trim().replaceAll("[\\.\\(]"," ").replaceAll("[^A-Za-z\\s]+", "").split("\\s+");
        occurs = getOccurances(commonCodeTags,currentSnippet.getText()) + getOccurances(codeTags.get(currentSnippet.getLanguage()),currentSnippet.getText());
        
        if(wordsOnly.length == 0)
        {
        	System.out.println("NaN "+currentSnippet.getText());
        	return 0;
        }
        
        double perc = occurs/(double)wordsOnly.length * 100;
        
        if(perc < 10)
        {
        	System.out.println(currentSnippet.getText());
        	return 0;
        }
        rating += perc/10;
        
        if (has("jframe", currentSnippet.getText()) && !has("setDefaultCloseOperation", currentSnippet.getText()))
            rating -= 100;
        
        System.out.print(rating+" ||");
        System.out.println(occurs+" "+wordsOnly.length+" : "+perc);

        return rating;
    }

    protected boolean getRecursive() {
        return false; //TODO write recursive check function
    }

    protected String getSearchLanguage() {
        return currentSnippet.getSearchLanguage();
    }

    protected String getLanguage() {
        String bestLanguageSoFar = "Unknown";
        int ratingSoFar = 0;

        for (String language : codeTags.keySet()) {
            int ratingForThisLanguage = 0;
            String[] tags = codeTags.get(language);

            for (String tag : tags) {
                ratingForThisLanguage += getOccurances(tag, currentSnippet.getText());
            }
            if (ratingForThisLanguage > ratingSoFar) {
                bestLanguageSoFar = language;
                ratingSoFar = ratingForThisLanguage;
            }
        }
        
        return bestLanguageSoFar;
    }

    protected boolean has(String tag, String snippet) {
        return snippet.toLowerCase().contains(tag.toLowerCase());
    }

    private int getOccurances(String tag, String snippet) {
        //reference: http://stackoverflow.com/questions/5223815/how-do-i-count-the-number-of-times-a-sequence-occurs-in-a-java-string
        tag = tag.toLowerCase();
        snippet = snippet.toLowerCase();
        int index = snippet.indexOf(tag);
        int count = 0;
        while (index != -1) {
            count++;
            snippet = snippet.substring(index + 1);
            index = snippet.indexOf(tag);
        }
        return count;
    }
    
    private int getOccurances(String[] tags, String snippet) {
    	int i = 0;
    	for(String tag : tags)
    		i += getOccurances(tag, snippet);
    	return i;
    }

    public static void initialise() {
        codeTags = new HashMap<>();

        javaCodeTags = new String[]{"System.out.print", "this", "super", "final ","package ","import ",".class"};
        cppCodeTags = new String[]{"auto ", "register", "sizeof", "const ","using ","include","struct "};
        pythonCodeTags = new String[]{"def ", "self(" , "self ", "self.","from ","import "};
        csharpCodeTags = new String[]{"base ", "console", "const ","using ", "type ","var ","dynamic ","struct "};

        codeTags.put("Java", javaCodeTags);
        codeTags.put("C++", cppCodeTags);
        codeTags.put("Python", pythonCodeTags);
        codeTags.put("C#", csharpCodeTags);
        codeTags.put("Unknown", new String[]{});
    }
}
