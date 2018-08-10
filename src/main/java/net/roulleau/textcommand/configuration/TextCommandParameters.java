package net.roulleau.textcommand.configuration;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import net.roulleau.textcommand.exception.ConfigurationException;

public class TextCommandParameters {

    public static final String CONFIGURATION_FILE_PARAMETER_NAME = "configurationfile";
   
    public static final int DEFAULT_HTTP_PORT = 8080;
    
    public static final String DEFAULT_FILENAME_PROPERTIES = "textcommand.conf";
    
    @TextCommandParameter("http.enabled")
    private Boolean isHttpEnabled = false;
    
    @TextCommandParameter("http.port")
    private Integer httpPort = DEFAULT_HTTP_PORT;
    
    @TextCommandParameter("dbus.enabled")
    private Boolean isDbusEnabled = false;

    @TextCommandParameter(CONFIGURATION_FILE_PARAMETER_NAME)
    private String configurationFile = DEFAULT_FILENAME_PROPERTIES;

    @TextCommandParameter("sms.enabled")
    private Boolean smsEnabled = false;
    
    @TextCommandParameter("sms.authorizedsenders")
    private List<String> authorizedSenders;
    
    public List<String> getAuthorizedSenders() {
        return authorizedSenders;
    }

    public String getConfigurationFile() {
        return configurationFile;
    }

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
                Optional<Object> parameterValue = tcFiller.getValue(parameterName);
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

    private <T> T convertValue(Object parameterValue, Class<T> type) throws ConfigurationException {
        
        if (type == String.class) {
            return type.cast(parameterValue.toString());
        } else if (type == Integer.class){
            return type.cast(Integer.parseInt(parameterValue.toString()));
        } else if (type == Boolean.class) {
            return type.cast(Boolean.parseBoolean(parameterValue.toString()));
        } else if (type == List.class) {
            if (parameterValue instanceof List) {
                return type.cast(parameterValue);
            } else {
                return type.cast(Arrays.asList(parameterValue));
            }
        }else {
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

    public boolean isSmsEnabled() {
        return smsEnabled;
    }
    
}
