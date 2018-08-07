package net.roulleau.textcommand.parser;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer<T> {
    
    LinkedList<TokenInfo<T>> tokenInfos = new LinkedList<TokenInfo<T>>();
    
    
    public void addToken(String regex, T token) {
        tokenInfos.add(new TokenInfo<T>(Pattern.compile("^(" + regex + ")"), token));
    }
    
    public LinkedList<Token<T>> tokenize(String str) {
        
        LinkedList<Token<T>> tokens = new LinkedList<Token<T>>();
        
        String s = str.trim();
        
        while (!s.equals("")) {
            boolean match = false;
            for (TokenInfo<T> info : tokenInfos) {
                Matcher m = info.regex.matcher(s);
                if (m.find()) {
                    match = true;
                    String tok = m.group();
                    s = m.replaceFirst("");
                    tokens.add(new Token<T>(info.token, tok));
                    break;
                }
            }
            if (!match) {
                throw new ParserException("Unexpected character in input: " + s);
            }
        }
        
        return tokens;
    }

}
