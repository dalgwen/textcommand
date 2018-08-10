package net.roulleau.textcommand.configuration;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.roulleau.textcommand.configuration.TextCommandParameters.TextCommandParameterWithField;

public class TextCommandParametersCommandLine implements TextCommandParameterFiller {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextCommandParameterFile.class);
    
    private OptionSet optionSet;
    
    public OptionSet getOptionSet() {
        return optionSet;
    }
    
    public TextCommandParametersCommandLine(String args[], List<TextCommandParameterWithField> parametersAvailable) {
        
        LOGGER.info("Trying to parse configuration in program arguments");
        
        OptionParser parser = new OptionParser();
        for (TextCommandParameterWithField parameterField : parametersAvailable) {
            String parameterName = parameterField.annotation.value();
            if (parameterField.field.getType() == Boolean.class) {
                parser.accepts(parameterName).withOptionalArg();
            }
            else {
                parser.accepts(parameterName).withRequiredArg();
            }            
        }        
        optionSet = parser.parse(args);        
    }

    @Override
    public Optional<Object> getValue(String parameterName) {
        
        Object valueRaw = optionSet.valueOf(parameterName);
        
        Optional<Object> parameterValue = Optional.ofNullable(valueRaw);
        if (parameterValue.isPresent()) {
            return parameterValue;
        }
        if (optionSet.has(parameterName)) {
            return Optional.of(Boolean.TRUE.toString());
        }
        return Optional.empty();
    }

}
