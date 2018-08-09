package net.roulleau.textcommand.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;

public class TextCommandParameterClassPath extends TextCommandParameterJavaProperties {

    public TextCommandParameterClassPath() {
        
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("textcommand.conf")) {
            if (is != null) {
                properties.load(is);
            }
        } catch (IOException | IllegalArgumentException e) {
            throw new InvalidParameterException("Cannot read textcommand.conf from classpath");
        };
    }

}
