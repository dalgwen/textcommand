package net.roulleau.textcommand.configuration;

import java.util.List;
import java.util.Optional;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.roulleau.textcommand.configuration.TextCommandParameters.TextCommandParameterWithField;

public class TextCommandParametersCommandLine implements TextCommandParameterFiller {

    private OptionSet optionSet;
    
    public TextCommandParametersCommandLine(String args[], List<TextCommandParameterWithField> parametersAvailable) {
        
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
    public Optional<String> getValue(String parameterName) {
        
        Optional<String> parameterValue = Optional.ofNullable((String)optionSet.valueOf(parameterName));
        if (parameterValue.isPresent()) {
            return parameterValue;
        }
        if (optionSet.has(parameterName)) {
            return Optional.of(Boolean.TRUE.toString());
        }
        return Optional.empty();
    }

}
