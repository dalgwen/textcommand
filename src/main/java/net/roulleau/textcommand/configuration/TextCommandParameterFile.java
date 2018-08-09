package net.roulleau.textcommand.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.Properties;

public class TextCommandParameterFile extends TextCommandParameterJavaProperties {

    public TextCommandParameterFile() {
        
        Path currentWorkingDirConfFile = Paths.get("" + "textcommand.conf").toAbsolutePath();
        if (Files.exists(currentWorkingDirConfFile)) {
            properties = new Properties();
            try (InputStream input = new FileInputStream("config.properties")) {
                properties.load(input);
            } catch (IOException | IllegalArgumentException e) {
                throw new InvalidParameterException("Cannot read properties in the file textcommand.conf in current directory");
            };
        }
    }

}
