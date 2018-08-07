package net.roulleau.textcommand.input.dbus;

import org.freedesktop.dbus.exceptions.DBusExecutionException;

public class DBusCustomException extends DBusExecutionException {

    private static final long serialVersionUID = -1204339563103294205L;

    public DBusCustomException(String message) {
        super(message);
    }
    
    public DBusCustomException(Exception cause) {
        super(cause.getMessage());
    }
    

}
