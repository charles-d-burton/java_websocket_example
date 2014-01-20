/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minewebsocket;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Scanner;

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
}
