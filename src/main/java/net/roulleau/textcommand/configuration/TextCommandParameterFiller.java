package net.roulleau.textcommand.configuration;

import java.util.Optional;

public interface TextCommandParameterFiller {

    Optional<String> getValue(String parameterName);

}
