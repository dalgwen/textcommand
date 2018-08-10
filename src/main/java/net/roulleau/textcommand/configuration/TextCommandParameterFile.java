package net.roulleau.textcommand.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextCommandParameterFile extends TextCommandParameterJavaProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextCommandParameterFile.class);
    
    public TextCommandParameterFile() {
        
        Path currentWorkingDirConfFile = Paths.get("textcommand.conf").toAbsolutePath();
        if (Files.exists(currentWorkingDirConfFile)) {
            LOGGER.info("Found configuration file in the working directory");
            properties = new Properties();
            try (InputStream input = new FileInputStream(currentWorkingDirConfFile.toFile())) {
                properties.load(input);
            } catch (IOException | IllegalArgumentException e) {
                throw new InvalidParameterException("Cannot read properties in the file textcommand.conf in current directory");
            };
        }
    }

}
