/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minewebsocket.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;
import minewebsocket.interfaces.JSONListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import minewebsocket.interfaces.ConnectedCallback;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

/**
 *
 * @author charles
 */
public class MessageHandler implements ConnectedCallback , Runnable{
    
    private static Connection connection = null;
    private LinkedList<JSONListener> listeners = new LinkedList();
    private BlockingQueue<String> queue = null;
    
    public MessageHandler(String hostname, int port) throws URISyntaxException, InterruptedException {
        System.out.println(hostname);
        queue = new ArrayBlockingQueue<String>(100);
        connection = new Connection(new URI("ws://" + hostname + ":" + port), new Draft_10());
    }
    
    //Hook classes in that want to receive the data.
    public void registerListener(JSONListener conn) {
        listeners.addLast(conn);
    }
    
    public boolean removeListener(JSONListener conn) {
        return listeners.remove(conn);
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
        queue.offer(message);
    }
    
    //Send a message to a pin, command whether or not a response is expected
    public void sendToPin(int value, int pin, boolean response, int ... read) {
        HashMap <String, HashMap> write = new HashMap();
        HashMap <Integer, Object[]> values = new HashMap();
        
        Object vars[] = new Object[2];
        vars[0] = value;
        vars[1] = Boolean.valueOf(response);
        
        values.put(Integer.valueOf(pin), vars);
        write.put("write", values);
        Gson gson = new Gson();
        String messageToSend = gson.toJson(write);
        boolean offer = queue.offer(messageToSend);
        if (!offer) System.out.println("Rejected from queue");
    }
    
    //Method to trigger a pin and broadcast the result
    public void sendToPin(int value, int pin, boolean response, boolean broadcast
            , int ... read) {
        
    }
    
    //Method to trigger pin, set broadcast, and set timeout for how long pin should be active
    public void sendToPin(int value, int pin, boolean response, boolean broadcast
            , long triggerTime, int ... read) {
        
    }
    
    //Send a message to write to a log
    public void sendLogMessage(String message) {
        JsonObject jobj = new JsonObject();
        jobj.addProperty("log", message);
        String value  = jobj.toString();
        queue.offer(message);
    }
    
    //Send a broadcast message
    public void broadcastMessage(String message) {
        JsonObject jobj = new JsonObject();
        jobj.addProperty("broadcast", message);
        String value = jobj.toString();
        queue.offer(message);
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

    @Override
    public void run() {
        Thread t = new Thread(new QueueManager());
        t.start();
    }
    
    public class Connection extends WebSocketClient {
    
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
    
    private class QueueManager implements Runnable {
        @Override
        public void run() {
            try {
                openConn();
            } catch (InterruptedException ex) {
                Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Connection opened successfully: Moving on");
            while (true) {
                try {
                    connection.send(queue.take());
                } catch (InterruptedException ex) {
                    Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    }
    
    //A simple waitlock so that I'm not trying to send messaged to a broken connection
    private synchronized boolean openConn() throws InterruptedException {
        return connection.connectBlocking();
    }
}
