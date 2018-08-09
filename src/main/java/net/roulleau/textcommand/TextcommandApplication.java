package net.roulleau.textcommand;

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
import net.roulleau.textcommand.exception.CommandExecutionException;
import net.roulleau.textcommand.exception.ParameterException;
import net.roulleau.textcommand.input.dbus.DBusListener;
import net.roulleau.textcommand.input.http.JettyServer;

public class TextcommandApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextcommandApplication.class);

    private static String[] commandLine;

    private static TextCommandParameters textCommandParameters = new TextCommandParameters();

    public static void main(String[] args) throws Exception {

        LOGGER.info("Starting TextCommand application");
        commandLine = args;

        readProperties();

        register();

        if (textCommandParameters.isHttpEnabled()) {
            startEmbeddedHttpServer(textCommandParameters.getHttpPort());
        }

        if (textCommandParameters.isDbusEnabled()) {
            startDbusListener();
        }

        LOGGER.info("Now waiting for text command input");
        while (true) {
            Thread.sleep(1000);
        }

    }

    private static void readProperties() throws ParameterException {

        // from file
        TextCommandParameterFiller tcpFile = new TextCommandParameterFile();
        textCommandParameters.fillWith(tcpFile);

        // from file in classpath
        TextCommandParameterFiller tcpFileClasspath = new TextCommandParameterClassPath();
        textCommandParameters.fillWith(tcpFileClasspath);

        // from system properties

        // from command line
        List<TextCommandParameterWithField> allParametersAvailable = textCommandParameters.getAllParametersAvailable();
        TextCommandParameterFiller tcpCommandLine = new TextCommandParametersCommandLine(commandLine, allParametersAvailable);
        textCommandParameters.fillWith(tcpCommandLine);

    }

    private static void startDbusListener() throws DBusException {
        LOGGER.info("Starting dbus listener");
        new DBusListener();
        LOGGER.info("dbus listener started");
    }

    private static void register() {
        LOGGER.info("Starting registering commands");
        CommandRegister.registerClass(EchoCommands.class);
        LOGGER.info("End of registering commands");
    }

    private static void startEmbeddedHttpServer(int port) throws Exception {
        LOGGER.info("Starting embedded http server");
        JettyServer jettyServer = new JettyServer(port);
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
    }

    public static Report execute(String textCommand) throws CommandExecutionException {
        return CommandExecutor.findAndExecute(textCommand);
    }

}
