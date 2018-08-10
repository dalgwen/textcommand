package net.roulleau.textcommand;

import java.util.LinkedList;
import java.util.List;

import org.freedesktop.dbus.exceptions.DBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.roulleau.textcommand.configuration.TextCommandParameterClassPath;
import net.roulleau.textcommand.configuration.TextCommandParameterFile;
import net.roulleau.textcommand.configuration.TextCommandParameterFiller;
import net.roulleau.textcommand.configuration.TextCommandParameters;
import net.roulleau.textcommand.configuration.TextCommandParameters.TextCommandParameterWithField;
import net.roulleau.textcommand.configuration.TextCommandParametersCommandLine;
import net.roulleau.textcommand.defaultcommands.EchoCommands;
import net.roulleau.textcommand.exception.ConfigurationException;
import net.roulleau.textcommand.input.dbus.DBusListener;
import net.roulleau.textcommand.input.http.JettyServer;

public class TextcommandApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextcommandApplication.class);

    private TextCommandParameters parameters;
        
    private CommandExecutor commandExecutor;

    public TextCommandParameters getParameters() {
        return parameters;
    }

    public static void main(String[] args) throws Exception {

        LOGGER.info("Starting TextCommand application");
        
        TextcommandApplication textCommandApplication = new TextcommandApplication();
        textCommandApplication.start(args);
    }
    
    public void start(String[] args) throws Exception {
        
        parameters = readConfiguration(args);

        CommandStore commandStore = registerCommands();
        commandExecutor = new CommandExecutor(commandStore);

        if (parameters.isHttpEnabled()) {
            startEmbeddedHttpServer();
        }

        if (parameters.isDbusEnabled()) {
            startDbusListener();
        }

        LOGGER.info("Now waiting for text command input");
        while (true) {
            Thread.sleep(1000);
        }

    }

    private TextCommandParameters readConfiguration(String[] args) throws ConfigurationException {

        LOGGER.info("Reading configuration");
        
        TextCommandParameters textCommandParameters = new TextCommandParameters();
        List<TextCommandParameterWithField> allParametersAvailable = TextCommandParameters.getAllParametersAvailable();

        LinkedList<TextCommandParameterFiller> configurationProviders = new LinkedList<TextCommandParameterFiller>();
        
        // from file
        configurationProviders.add(new TextCommandParameterFile());
        // from file in classpath
        configurationProviders.add(new TextCommandParameterClassPath());
        // from command line
        configurationProviders.add(new TextCommandParametersCommandLine(args, allParametersAvailable));

        for (TextCommandParameterFiller tcpf : configurationProviders) {
            textCommandParameters.fillWith(tcpf);
        }
        
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
