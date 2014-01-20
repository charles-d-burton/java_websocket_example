/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minewebsocket.interfaces;

/**
 *
 * @author charles
 */
public interface JSONListener {
    public void messageReceived(String message);
    public void connected(boolean status);
    public void connectionClosed();
}
