package net.roulleau.textcommand.parser;

public enum SimpleGrammar {
    
    OR("\\([" + Constants.wordsRegex + "\\ ]*\\|[" + Constants.wordsRegex + "\\ ]*\\)"),
    VARIABLE("\\$[" + Constants.wordsRegex + "]+"),
    STRING("[\\s" + Constants.wordsRegex + "]+");
    
    final private String regex;
    
    public String getRegex() {
        return regex;
    }

    SimpleGrammar(String regex) {
        this.regex = regex;
    }
    
    public static class Constants {
        final static String wordsRegex = "a-zA-Z0-9-'";
    }
    
}