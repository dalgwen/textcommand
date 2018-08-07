package net.roulleau.textcommand.input.dbus;

import org.freedesktop.DBus;
import org.freedesktop.dbus.DBusInterface;

public interface DBusTextCommandInterface extends DBusInterface {
    
    @DBus.Description("Can execute commands previously defined in code")
    public String executeCommand(String command);
}
