package net.roulleau.textcommand.parser;

public class Token<T> {

    public final T token;

    public final String sequence;

    public Token(T token, String sequence) {
        super();
        this.token = token;
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return "Token [token=" + token + ", sequence=" + sequence + "]";
    }

    
}