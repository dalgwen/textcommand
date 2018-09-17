package net.roulleau.textcommand.configuration;

import java.util.List;

import net.roulleau.gmpe.Parameter;

public class TextCommandParameters {

    public static final String CONFIGURATION_FILE_PARAMETER_NAME = "configurationfile";
   
    public static final int DEFAULT_HTTP_PORT = 8080;
    
    public static final String DEFAULT_FILENAME_PROPERTIES = "textcommand.conf";
    
    @Parameter("http.enabled")
    private Boolean isHttpEnabled = false;
    
    @Parameter("http.port")
    private Integer httpPort = DEFAULT_HTTP_PORT;
    
    @Parameter("dbus.enabled")
    private Boolean isDbusEnabled = false;

    @Parameter(CONFIGURATION_FILE_PARAMETER_NAME)
    private String configurationFile = DEFAULT_FILENAME_PROPERTIES;

    @Parameter("sms.enabled")
    private Boolean smsEnabled = false;
    
    @Parameter("sms.authorizedsenders")
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
    
    public boolean isSmsEnabled() {
        return smsEnabled;
    }
    
}
