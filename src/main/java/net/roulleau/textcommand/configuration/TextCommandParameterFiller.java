package net.roulleau.textcommand.configuration;

import java.util.Optional;

public interface TextCommandParameterFiller {

    Optional<Object> getValue(String parameterName);

}
