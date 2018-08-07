package net.roulleau.textcommand.parser;

import java.util.LinkedList;

public class CommandParser {

    Tokenizer<SimpleGrammar> tokenizer;
    
    public CommandParser() {
        tokenizer = new Tokenizer<SimpleGrammar>();
        for (SimpleGrammar grammarElement : SimpleGrammar.values()) {
            tokenizer.addToken(grammarElement.getRegex(), grammarElement);
        }
        
    }
    
    public LinkedList<Token<SimpleGrammar>> parseCommand(String command) {
        LinkedList<Token<SimpleGrammar>> tokens = tokenizer.tokenize(command);
        return tokens;        
    }
    
    
}
