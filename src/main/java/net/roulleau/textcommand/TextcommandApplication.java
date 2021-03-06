package net.roulleau.textcommand;

import org.freedesktop.dbus.exceptions.DBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.roulleau.gmpe.Gmpe;
import net.roulleau.gmpe.sources.ParameterSourceClassPath;
import net.roulleau.gmpe.sources.ParameterSourceFile;
import net.roulleau.gmpe.sources.ParameterSourceCommandLine;
import net.roulleau.textcommand.configuration.TextCommandParameters;
import net.roulleau.textcommand.defaultcommands.EchoCommands;
import net.roulleau.textcommand.exception.ConfigurationException;
import net.roulleau.textcommand.input.dbus.DBusListener;
import net.roulleau.textcommand.input.http.JettyServer;
import net.roulleau.textcommand.input.sms.GammuHandler;

public class TextcommandApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextcommandApplication.class);

    private TextCommandParameters parameters;
        
    private CommandExecutor commandExecutor;

    public TextCommandParameters getParameters() {
        return parameters;
    }

    public static void main(String[] args) throws DBusException, ConfigurationException {

        LOGGER.info("Starting TextCommand application");
        
        TextcommandApplication textCommandApplication = new TextcommandApplication();
        textCommandApplication.start(args);
    }
    
    public void start(String[] args) throws DBusException, ConfigurationException {
        
        parameters = readConfiguration(args);

        // registering commands and preparing a command executor:
        CommandStore commandStore = registerCommands();
        commandExecutor = new CommandExecutor(commandStore);

        // Daemon mode enable the program to wait for message after initialization
        boolean isInDaemonMode = false;
        
        //simple jetty server
        if (parameters.isHttpEnabled()) {
            startEmbeddedHttpServer();
            isInDaemonMode = true;
        }
        
        //simple dbus listner
        if (parameters.isDbusEnabled()) {
            startDbusListener();
            isInDaemonMode = true;
        }
        
        //using environnement property as source of text command, as gammu could have set
        if (parameters.isSmsEnabled()) { 
            handleSmsEnvironnmentProperties();
        }

        //daemon mode will wait for input from other enabled module
        while (true && isInDaemonMode) {
            LOGGER.info("Now waiting for text command input");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.info("Interrupted. Closing application");
            }
        }

    }

    private void handleSmsEnvironnmentProperties() {        
        new GammuHandler(commandExecutor, parameters.getAuthorizedSenders()).start();       
    }

    private TextCommandParameters readConfiguration(String[] args) throws ConfigurationException {

        LOGGER.info("Reading configuration");
        TextCommandParameters textCommandParameters = new TextCommandParameters();
        
        Gmpe.fill(textCommandParameters).with(
        		new ParameterSourceCommandLine(args),
        		new ParameterSourceFile(),
        		new ParameterSourceClassPath(),
        		new ParameterSourceCommandLine(args));
        
        return textCommandParameters;
    }

    private void startDbusListener() throws DBusException {
        LOGGER.info("Starting dbus listener");
        new DBusListener(commandExecutor);
        LOGGER.info("dbus listener started");
    }

    private CommandStore registerCommands() {
        CommandStore commandStore = new CommandStore();
        LOGGER.info("Starting registering commands");
        commandStore.addAll(CommandRegister.registerClass(EchoCommands.class));
        LOGGER.info("All commands registered");
        return commandStore;
    }

    private void startEmbeddedHttpServer() {
        LOGGER.info("Starting embedded http server");
        JettyServer jettyServer = new JettyServer(commandExecutor, parameters.getHttpPort());
        try {
            jettyServer.start();
            while (!jettyServer.isStarted() && !jettyServer.isStoppedOrStopping()) {
                Thread.sleep(100);
            }
            if (jettyServer.isStarted()) {
                LOGGER.info("Embedded http server is running");
            } else {
                if (jettyServer.isStoppedOrStopping()) {
                    LOGGER.info("Embedded http server start failed !?");
                }
            }
        } catch (Exception e) {
            LOGGER.error("Cannot start jetty server", e);
        }

    }
}
