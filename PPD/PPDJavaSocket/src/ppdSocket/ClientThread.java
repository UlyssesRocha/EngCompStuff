/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppdSocket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author ulysses
 */
class ClientThread extends Thread
{  
    private Socket           socket   = null;
    private Client       client   = null;
    private DataInputStream  streamIn   = null;
    private DataOutputStream streamOut = null;

    public ClientThread(Client _client, Socket _socket)
    {  
        client   = _client;
        socket   = _socket;
        open();  
        start();
    }
   
    public void open()
    {  
        try{  
            streamIn = new DataInputStream(new 
                            BufferedInputStream(socket.getInputStream()));

            streamOut = new DataOutputStream(new
                            BufferedOutputStream(socket.getOutputStream()));
        }
        catch(IOException ioe)
        {  
            System.out.println("Error getting input stream: " + ioe);
        }
    }
    
        // Sends message to Server
    public void send(String msg){   
        try
        {  
            streamOut.writeUTF(msg);
            streamOut.flush();
        }
       
        catch(IOException ioexception)
        {  
            System.out.println(" ERROR sending message: " + ioexception.getMessage());
            stop();
        }
    }
    
    public void close()
    {  
        try
        {  
            if (streamIn != null) streamIn.close();
        }
      
        catch(IOException ioe)
        {  
            System.out.println("Error closing input stream: " + ioe);
        }
    }
    
    public void run()
    {  
        while (true)
        {   try
            {  
                client.handle(streamIn.readUTF());
            }
            catch(IOException ioe)
            {  
                System.out.println("Listening error: " + ioe.getMessage());
            }
        }
    }
}


