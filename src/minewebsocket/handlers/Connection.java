/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minewebsocket.handlers;

import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

/**
 *
 * @author charles
 */
public class Connection extends WebSocketClient{
    
    public Connection(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }
    
    public Connection(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println( "opened connection\n\n\n");
    }

    @Override
    public void onMessage(String message) {
        System.out.println( "\nreceived: " + message + "\n");
        System.out.print("%>");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println( "Connection closed by " + ( remote ? "remote peer" : "me" ) );
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
    
}
