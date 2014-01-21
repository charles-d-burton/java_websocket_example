/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minewebsocket;

import java.net.URISyntaxException;
import minewebsocket.handlers.MessageHandler;
import minewebsocket.interfaces.JSONListener;

/**
 *
 * @author charles
 */
public class RunTest implements JSONListener{
    MessageHandler mh = null;
    public RunTest(String hostname, int port) throws URISyntaxException, InterruptedException {
        mh = new MessageHandler(hostname, port);
        mh.registerListener(this);
    }
    
    public void startTest(){
        mh.getFromPins(1,2,3);
        mh.sendToPin("Pin test", 1, true);
        mh.sendLogMessage("Log Test");
        mh.broadcastMessage("Broadcast Test");
       // mh.closeConnection();
        
    }

    @Override
    public void messageReceived(String message) {
        System.out.println("From Listener: " + message);
    }

    @Override
    public void connected(boolean status) {
        System.out.println("Connection Confirmed");
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void connectionClosed() {
        System.out.println("Connection Closed");
    }
}
