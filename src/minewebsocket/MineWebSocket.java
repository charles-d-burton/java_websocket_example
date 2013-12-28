/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minewebsocket;

import java.net.URI;
import java.net.URISyntaxException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

/**
 *
 * @author charles
 */
public class MineWebSocket extends WebSocketClient{
    private static MineWebSocket c = null;
    private static final int max = 50;
    private static int count = 0;
    public MineWebSocket(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }
    
    public MineWebSocket(URI serverURI) {
        super(serverURI);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        c = new MineWebSocket(new URI("ws://raspberrypi:5000"), new Draft_10());
        c.connectBlocking();
        c.send("things");
        
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println( "opened connection" );
    }

    @Override
    public void onMessage(String message) {
         System.out.println( "received: " + message );
         c.close();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) );
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
