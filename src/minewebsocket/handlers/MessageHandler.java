/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minewebsocket.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

/**
 *
 * @author charles
 */
public class MessageHandler implements Connections{
    
    private static Connection connection = null;
    private LinkedList<Connections> listeners = new LinkedList();
    
    public MessageHandler(String hostname, int port) throws URISyntaxException, InterruptedException {
        connection = new Connection(new URI("ws://" + hostname + ":" + port), new Draft_10());
        connection.connect();
    }
    
    //Take an arbitrary number of pins, construct a JSON message and send it to the connection
    public void getFromPins(int ... pins) {
        if (pins.length == 0) return; //Safety check
        HashMap<String, Integer[]> reader = new HashMap();
        
        Integer pinNums[] = new Integer[pins.length];
        
        for (int i = 0; i < pins.length; i++) {
            pinNums[i] = Integer.valueOf(pins[i]);
        }       
        reader.put("read", pinNums);
        Gson gson = new Gson();
        String message = gson.toJson(reader);
        if (connection.isOpen()) {
            connection.send(message);
        } else {
            
        }
    }
    
    //Send a message to a pin, command whether or not a response is expected
    public void sendToPin(String message, int pin, boolean respond) {
        HashMap <String, HashMap> write = new HashMap();
        HashMap <Integer, Object[]> values = new HashMap();
        
        Object vars[] = new Object[2];
        vars[0] = message;
        vars[1] = Boolean.valueOf(respond);
        
        values.put(Integer.valueOf(pin), vars);
        write.put("write", values);
        Gson gson = new Gson();
        String messageToSend = gson.toJson(write);
        
        if (connection.isOpen()) {
            connection.send(message);
        } else {
            
        }
    }
    
    //Send a message to write to a log
    private void sendLogMessage(String message) {
        JsonObject jobj = new JsonObject();
        jobj.addProperty("log", message);
        String value  = jobj.toString();
        if (connection.isOpen()) {
            connection.send(value);
        }
    }
    
    //Send a broadcast message
    private void broadcastMessage(String message) {
        JsonObject jobj = new JsonObject();
        jobj.addProperty("broadcast", message);
        String value = jobj.toString();
        if (connection.isOpen()){
            connection.send(value);
        }
    }

    @Override
    public boolean isConnected() {
        if (connection.isOpen()) return true;
        return false;
    }
    
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
}
