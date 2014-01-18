/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minewebsocket;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    /**
     * @param args the command line arguments
     * Replace this bit with whatever method you want and you should be able to use
     * it as is.
     */
    public static void main(String[] args) throws URISyntaxException, InterruptedException, IOException {
        Connection c = setupConnection();
        c.connectBlocking();
        String input = null;
        while (c.isOpen()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("%>");
            while ((input = br.readLine()) != null && c.isOpen()) {
                Gson gson = new Gson();
                if (input.equalsIgnoreCase("end")) break;
                c.send(input);
                
            }
            if (input.equalsIgnoreCase("end")) break;
        }
        c.close();
    }
    
    private static Connection setupConnection() throws URISyntaxException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Host: ");
        String host = scan.nextLine();
        System.out.print("Port: ");
        String port = scan.nextLine();
        //scan.close();
        return new Connection(new URI("ws://" + host + ":" + port), new Draft_10());
    }
    
    private static String buildMessage() {
        HashMap values = new HashMap();
        Scanner scan = new Scanner(System.in);
        String value = "";
        while (!value.equals("end")) {
            System.out.print("Pin: ");
            value = scan.nextLine();
            String pin = value;
            
        }
        
        return null;
    }
}
