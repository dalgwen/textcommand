package net.roulleau.textcommand.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.freedesktop.dbus.exceptions.DBusException;
import org.junit.Test;

import net.roulleau.textcommand.TextcommandApplication;
import net.roulleau.textcommand.exception.ConfigurationException;

public class ConfigurationTest {

    @Test
    public void test() throws DBusException, ConfigurationException {
        String[] args = new String[] {"--" + TextCommandParameters.CONFIGURATION_FILE_PARAMETER_NAME, "target/test-classes/testconfigurationfile.conf"};
        TextcommandApplication tca = new TextcommandApplication();
        tca.start(args);
        assertThat(tca.getParameters().getHttpPort()).isEqualTo(8282);
    }

}
