package net.roulleau.textcommand.configuration;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import net.roulleau.textcommand.exception.ConfigurationException;

public class TextCommandParameters {

    public static final int DEFAULT_HTTP_PORT = 8080;
    
    @TextCommandParameter("http.enabled")
    private Boolean isHttpEnabled = false;
    
    @TextCommandParameter("http.port")
    private Integer httpPort = DEFAULT_HTTP_PORT;
    
    @TextCommandParameter("dbus.enabled")
    private Boolean isDbusEnabled = false;

    public boolean isHttpEnabled() {
        return isHttpEnabled;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public boolean isDbusEnabled() {
        return isDbusEnabled;
    }
    
    public void fillWith(TextCommandParameterFiller tcFiller) throws ConfigurationException {
        
        for (Field field : TextCommandParameters.class.getDeclaredFields()) {
            TextCommandParameter annotation = field.getAnnotation(TextCommandParameter.class);
            
            if (annotation != null) {
                String parameterName = annotation.value();
                Optional<String> parameterValue = tcFiller.getValue(parameterName);
                if (parameterValue.isPresent()) {
                    Object paramaterValuedConverted = convertValue(parameterValue.get(), field.getType()); 
                    try {
                        field.set(this, paramaterValuedConverted);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        throw new ConfigurationException("Cannot access parameter " + parameterName);
                    }
                }
            }
        }        
    }

    private Object convertValue(String parameterValue, Class<?> type) throws ConfigurationException {
        if (type == String.class) {
            return parameterValue;
        } else if (type == Integer.class){
            return Integer.parseInt(parameterValue);
        } else if (type == Boolean.class) {
            return Boolean.parseBoolean(parameterValue);
        } else {
            throw new ConfigurationException("Runtime error : type " + type.toString() + " not accepted");
        }
    }
    
    public static List<TextCommandParameterWithField> getAllParametersAvailable() {
        
        return Arrays.stream(TextCommandParameters.class.getDeclaredFields())
                .map(field -> new TextCommandParameterWithField(field.getAnnotation(TextCommandParameter.class), field))
                .filter(field -> field.annotation != null)
                .collect(Collectors.toList());
    }
    
    public static class TextCommandParameterWithField {
        public TextCommandParameter annotation;
        public Field field;
        
        public TextCommandParameterWithField(TextCommandParameter annotation, Field field) {
            this.annotation = annotation;
            this.field = field;
        }
    }
    
}
