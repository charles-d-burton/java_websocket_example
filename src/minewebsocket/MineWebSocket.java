/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minewebsocket;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Scanner;
import minewebsocket.handlers.Connection;
import org.java_websocket.drafts.Draft_10;

/**
 *
 * @author charles
 */
public class MineWebSocket {
    //Ugly hack,never do this for real
    private static Scanner scan = new Scanner(System.in);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws URISyntaxException, InterruptedException, IOException {
        Connection c = setupConnection();
        c.connectBlocking();
        String input = null;
        while (c.isOpen()) {
            c.send(buildMessage());
        }
        c.close();
    }
    
    //Gets some basic connection info and esatablishes a connection with remote host
    private static Connection setupConnection() throws URISyntaxException {
        System.out.print("Host: ");
        String host = scan.nextLine();
        System.out.print("Port: ");
        String port = scan.nextLine();
        return new Connection(new URI("ws://" + host + ":" + port), new Draft_10());
    }
    
    private static String buildMessage() {
        HashMap values = new HashMap();
        System.out.print("Pin Number: ");
        String pin = scan.nextLine();
        System.out.print("Value: ");
        String value = scan.nextLine();
        values.put(pin, value);
            
        Gson gson = new Gson();
        String json = gson.toJson(values);
        System.out.println(json);
        return json;
    }
    
    private static HashMap buildPinReadMessage(int pin) {
        return null;
    }
    
    private static HashMap buildPinWriteMessage(int pin, String message) {
        return null;
    }
    
    private static String buildLogMessage(String message) {
        return null;
    }
}
