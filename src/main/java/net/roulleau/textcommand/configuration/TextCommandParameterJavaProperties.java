package net.roulleau.textcommand.configuration;

import java.util.Optional;
import java.util.Properties;

public abstract class TextCommandParameterJavaProperties implements TextCommandParameterFiller {
    
    protected Properties properties;

    @Override
    public Optional<String> getValue(String parameterName) {
        if (properties != null) {
            return Optional.ofNullable(properties.getProperty(parameterName));
        } else {
            return Optional.empty();
        }
    }
}
