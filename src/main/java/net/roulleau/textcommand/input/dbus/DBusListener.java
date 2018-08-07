package net.roulleau.textcommand.input.dbus;

import org.freedesktop.DBus;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.roulleau.textcommand.Report;
import net.roulleau.textcommand.TextcommandApplication;
import net.roulleau.textcommand.exception.CommandExecutionException;

public class DBusListener implements DBusTextCommandInterface {

    Logger LOGGER = LoggerFactory.getLogger(DBusListener.class);
    
    private DBusConnection dbusConnection;
    
    public DBusListener() throws DBusException {
        dbusConnection = DBusConnection.getConnection(DBusConnection.SESSION);
        dbusConnection.requestBusName("net.roulleau.textcommand");
        dbusConnection.exportObject("/Command", this);
    }
    
    @Override
    @DBus.Description("Can execute commands previously defined in code")
    public String executeCommand(String command) {
        
        LOGGER.info("Receiving dbus call with command '{}'", command);
        
        try {
            Report result = TextcommandApplication.execute(command);
            if (result.getReturnedObject() != null) {
                return result.getReturnedObject().toString();
            } else {
                return result.toString();
            }
        } catch (CommandExecutionException e) {
            throw new DBusCustomException(e);
        }
    }

    @Override
    public boolean isRemote() {
        return false;
    }

    @Override
    public String getObjectPath() {
        return "net.roulleau.textcommand";
    }
    
}
