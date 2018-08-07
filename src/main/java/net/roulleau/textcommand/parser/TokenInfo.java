package net.roulleau.textcommand.parser;

import java.util.regex.Pattern;

class TokenInfo<T> {
    public final Pattern regex;

    public final T token;

    public TokenInfo(Pattern regex, T token) {
        super();
        this.regex = regex;
        this.token = token;
    }
}