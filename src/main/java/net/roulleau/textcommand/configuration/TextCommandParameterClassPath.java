package net.roulleau.textcommand.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextCommandParameterClassPath extends TextCommandParameterJavaProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextCommandParameterFile.class);
    
    public TextCommandParameterClassPath() {
        
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("textcommand.conf")) {
            if (is != null) {
                LOGGER.info("Found configuration file in the classpath");
                properties.load(is);
            }
        } catch (IOException | IllegalArgumentException e) {
            throw new InvalidParameterException("Cannot read textcommand.conf from classpath");
        };
    }

}
