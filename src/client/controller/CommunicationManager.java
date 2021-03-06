package client.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import common.model.Item;

/**
 * Manager communication for the client
 * 
 * @author Sean Kenny and Jean-David Rousseau
 * @version 1.0
 * @since April 4th 2019
 *
 */
public class CommunicationManager 
{
    /** the socket that this cManager talks with */
    private Socket socket;
    /** objects will be recieved from the server through this stream */
    private ObjectInputStream objectFromSocket;
    /** objects will be sent to the server through this stream */
    private ObjectOutputStream objectToSocket;
    
    /**
     * all values in the cManager are set to null
     */
    public CommunicationManager()
    {
        socket = null;
        objectFromSocket = null;
        objectToSocket = null;
    }
    
    /**
     * connects to a given IP and port number
     * 
     * @param serverName the IP address to connect to
     * @param portNumber the port number to use
     * @throws IOException
     */
    public void setUpConnection(String serverName, int portNumber) throws IOException
    {
        socket = new Socket(serverName, portNumber);
        objectFromSocket = new ObjectInputStream(socket.getInputStream());
        objectToSocket = new ObjectOutputStream(socket.getOutputStream());
    }
    
    /**
     * ends the connection with the server
     * 
     * @throws IOException
     */
    public void endConnection() throws IOException
    {
        objectFromSocket.close();
        objectToSocket.close();
        socket.close();
    }
    
    /**
     * reads an item that the server sent
     * 
     * @return the item read
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Item readItem() throws IOException, ClassNotFoundException
    {
        return (Item) readObject();
    }
    
    /**
     * reads a string that the server sent
     * 
     * @return the String the server sent
     * @throws IOException
     */
    public String readString() throws IOException
    {
        try
        {
            return (String) readObject();
        }
        catch(ClassNotFoundException cnfe)
        {
            //How is this even possible?
            System.err.println("HOW ON EARTH ARE YOU RUNNING JAVA WITHOUT THE STRING CLASS????????");
            System.exit(1);
            return "YOU SHOULD NEVER GET HERE!!!!";
        }
    }
    
    /**
     * reads a generic object from the socket
     * 
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Object readObject() throws IOException, ClassNotFoundException
    {
        return objectFromSocket.readObject();
    }
    
    /**
     * sends a string message to the server
     * 
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException
    {
        objectToSocket.writeObject(message);
    }
    
    /**
     * reads a sequence of items from the server
     * 
     * @return an ArrayList of items containing all the items the server sent
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ArrayList<Item> readSequenceOfItems() throws IOException, ClassNotFoundException
    {
        ArrayList<Item> itemsOnDisplay = new ArrayList<Item>();
        try
        {
            String message = "";
            do
            {
                message = readString();
                if(message.equals("INVALIDQUERY"))
                {
                    break;
                }
                Item sentItem = readItem();
                itemsOnDisplay.add(sentItem);
                sendMessage("GOTITEM");
            }while(message.equals("TASKINPROGRESS"));
            return itemsOnDisplay;
        }
        catch(NullPointerException npe)
        {
            //Null should be sent if the database is empty, so we don't need to do anything!
            return itemsOnDisplay;
        }
    }
    
    /**
     * gets the info of an item from the items ID
     * 
     * @param specifiedID the ID of the item we want to return
     * @return the item with the given ID
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Item readItemInfo(int specifiedID) throws IOException, ClassNotFoundException
    {
        sendMessage("REQUESTITEMINFO " + specifiedID);
        String messageFromServer = readString();
        if(messageFromServer.equals("INVALIDQUERY"))
        {
            return null;
        }
        else if(messageFromServer.equals("SENDINGITEM"))
        {
            return readItem();
        }
        else
        {
            return null;
        }
    }
    
    /**
     * sends a message for a login to the server and returns the message sent back by the server
     * 
     * @param username the username entered by the user
     * @param password the password entered by the user
     * @return the message the server sent back
     * @throws IOException
     */
    public String sendLoginMessageAndReturnServerOutput(String username, String password) throws IOException
    {
        sendMessage("LOGIN" + " " + username + " " + password);
        String messageFromServer = readString();
        return messageFromServer;
    }
}
