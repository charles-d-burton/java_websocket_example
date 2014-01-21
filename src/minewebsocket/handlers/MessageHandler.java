/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minewebsocket.handlers;

import minewebsocket.interfaces.JSONListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import minewebsocket.interfaces.ConnectedCallback;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

/**
 *
 * @author charles
 */
public class MessageHandler implements ConnectedCallback{
    
    private static Connection connection = null;
    private LinkedList<JSONListener> listeners = new LinkedList();
    
    public MessageHandler(String hostname, int port) throws URISyntaxException, InterruptedException {
        connection = new Connection(new URI("ws://" + hostname + ":" + port), new Draft_10());
        connection.connectBlocking();
    }
    
    //Hook classes in that want to receive the data.
    public void registerListener(JSONListener conn) {
        listeners.addLast(conn);
    }
    
    //Take an arbitrary number of pins, construct a JSON message and send it to 
    //the connection for a read request from all of the pins
    
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
    public void sendToPin(String message, int pin, boolean response) {
        HashMap <String, HashMap> write = new HashMap();
        HashMap <Integer, Object[]> values = new HashMap();
        
        Object vars[] = new Object[2];
        vars[0] = message;
        vars[1] = Boolean.valueOf(response);
        
        values.put(Integer.valueOf(pin), vars);
        write.put("write", values);
        Gson gson = new Gson();
        String messageToSend = gson.toJson(write);
        
        if (connection.isOpen()) {
            connection.send(messageToSend);
        } else {
            
        }
    }
    
    //Send a message to write to a log
    public void sendLogMessage(String message) {
        JsonObject jobj = new JsonObject();
        jobj.addProperty("log", message);
        String value  = jobj.toString();
        if (connection.isOpen()) {
            connection.send(value);
        }
    }
    
    //Send a broadcast message
    public void broadcastMessage(String message) {
        JsonObject jobj = new JsonObject();
        jobj.addProperty("broadcast", message);
        String value = jobj.toString();
        if (connection.isOpen()){
            connection.send(value);
        }
    }

    //Test to make sure that connection is open
    @Override
    public boolean isConnected() {
        if (connection.isOpen()) return true;
        return false;
    }

    @Override
    public void closeConnection() {
        for (JSONListener c: listeners) {
            c.connectionClosed();
        }
        connection.close();
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
            for (JSONListener c: listeners) {
                c.connected(true);
            }
        }

        @Override
        public void onMessage(String message) {
            //System.out.println( "\nreceived: " + message + "\n");
            for (JSONListener c : listeners) {
                c.messageReceived(message);
            }
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
