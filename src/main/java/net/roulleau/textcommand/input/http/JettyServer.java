package net.roulleau.textcommand.input.http;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class JettyServer {

    private static Server server;
    
    private int port;
    
    public JettyServer(int port) {
        this.port = port;
    }


    public void start() throws Exception {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[] {
                connector });
        
        Handler handler = new Handler();
        server.setHandler(handler);
                
        server.start();
    }

    
    public boolean isStarted() {
        return server.isStarted();
    }
    
    public boolean isStoppedOrStopping() {
        return server.isStopped() || server.isStopping();
    }
    
}
