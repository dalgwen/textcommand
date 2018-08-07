package net.roulleau.textcommand;

import org.freedesktop.dbus.exceptions.DBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.roulleau.textcommand.defaultcommands.EchoCommands;
import net.roulleau.textcommand.exception.CommandExecutionException;
import net.roulleau.textcommand.input.dbus.DBusListener;
import net.roulleau.textcommand.input.http.JettyServer;

public class TextcommandApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextcommandApplication.class);
    
    public static void main(String[] args) throws Exception {
        
        LOGGER.info("Starting TextCommand application");
        
        register();
        
        startEmbeddedHttpServer();
        
        startDbusListener();

        LOGGER.info("Now waiting for text command input");
        while (true) {
            Thread.sleep(1000);
        }
        
    }

    private static void startDbusListener() throws DBusException {
        new DBusListener();
    }

    private static void register() {
        LOGGER.info("Starting registering commands");
        CommandFinder.registerClass(EchoCommands.class);
        LOGGER.info("End of registering commands");
    }

    private static void startEmbeddedHttpServer() throws Exception {
        LOGGER.info("Starting embedded http server");
        JettyServer jettyServer = new JettyServer();
        jettyServer.start();
        while (! jettyServer.isStarted() && ! jettyServer.isStoppedOrStopping() ) {
            Thread.sleep(100);
        }
        if (jettyServer.isStarted()) {
            LOGGER.info("Embedded http server is running");
        } else {
            if (jettyServer.isStoppedOrStopping()) {
                LOGGER.info("Embedded http server start failed !?");
            }
        }
    }
    
    public static Report execute(String textCommand) throws CommandExecutionException {
        
        return CommandExecutor.findAndExecute(textCommand);
    }

}
