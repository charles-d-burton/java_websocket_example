/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minewebsocket.handlers;

import java.util.HashMap;

/**
 *
 * @author charles
 */
public class MessageHandler {
    
    public interface Connected {
        public void isConnected();
    }
    
    private static HashMap buildPinReadMessage(int pin) {
        return null;
    }
    
    private static HashMap buildPinWriteMessage(int pin, String message) {
        return null;
    }
    
    private static HashMap buildLogMessage(String message) {
        return null;
    }
    
    private static HashMap buildPassMessage(String message) {
        return null;
    }
    
}
