package net.roulleau.textcommand.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.freedesktop.dbus.exceptions.DBusException;
import org.junit.Test;

import net.roulleau.textcommand.TextcommandApplication;
import net.roulleau.textcommand.exception.ConfigurationException;

public class ConfigurationTest {

    @Test
    public void testConfigurationInExternalFile() throws DBusException, ConfigurationException {
        String[] args = new String[] {"--" + TextCommandParameters.CONFIGURATION_FILE_PARAMETER_NAME, "target/test-classes/testconfigurationfile.conf"};
        TextcommandApplication tca = new TextcommandApplication();
        tca.start(args);
        assertThat(tca.getParameters().getHttpPort()).isEqualTo(8282);
    }
    
    @Test
    public void testConfigurationSmsMultipleAuthorizedSenderInFile() throws DBusException, ConfigurationException {
        String[] args = new String[] {"--" + TextCommandParameters.CONFIGURATION_FILE_PARAMETER_NAME, "target/test-classes/testconfigurationfile.conf"};
        TextcommandApplication tca = new TextcommandApplication();
        tca.start(args);
        assertThat(tca.getParameters().getAuthorizedSenders()).containsOnly("012345678","087654321");
    }
    
    @Test
    public void testConfigurationList() throws DBusException, ConfigurationException {
        String[] args = new String[] {"--sms.authorizedsenders" , "013245678,087654321"};
        TextcommandApplication tca = new TextcommandApplication();
        tca.start(args);
        assertThat(tca.getParameters().getAuthorizedSenders()).containsOnly("013245678","087654321");
    }

}
