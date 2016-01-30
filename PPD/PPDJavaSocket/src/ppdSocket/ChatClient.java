/*
Client de chat simples, código de terceiros, nao modificado.
Autor desconhecido.

Utilizado para debug

Versao "texto para utilizar o servidor"
--Mensagens--
    Client->Server
    -c Connect+name [-cJoao da Silva] 
    -q Quit [-q]
    -m Message+content[-mOlá, tudo bem?]
    -p Play+cardNumber [-p02] //01 a 24

    Server->Client
    +a AcceptedConnection [+a]
    +r Ready for play [+r] //Broadcast
    +m Message+nameOfSource: +Content [+mJoao da Silva: Olá, tudo bem?]//broadcast
    +q PlayerQuitGame+name [+qJoao da Silva] 
    +y YourTurn [+y]
    +p Played+card+,+content[+p02,08] //Broadcast
    +i IllegalMove (play again) [+i]
    +f FailMove [+f] 
    +s SuccessMove+TotalPoints [+s9] (cards removed)
    +o OpponentSucessMove+Points [+o9] (cards removed)
    +l Lose (voce perdeu)
    +w Won (voce ganhou)
    +d Draw (empate)
    +e Error(mensagem desconhecida/corrompida/inapropriada) [+e] 
    +bye //enviado a quem se retira da partida
*/
package ppdSocket;


import java.net.*;
import java.io.*;


public class ChatClient implements Runnable
{  
    private Socket socket              = null;
    private Thread thread              = null;
    private DataInputStream  console   = null;
    private DataOutputStream streamOut = null;
    private ChatClientThread client    = null;

    public ChatClient(String serverName, int serverPort)
    {  
        System.out.println("Establishing connection to server...");
        
        try
        {
            // Establishes connection with server (name and port)
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected to server: " + socket);
            start();
        }
        
        catch(UnknownHostException uhe)
        {  
            // Host unkwnown
            System.out.println("Error establishing connection - host unknown: " + uhe.getMessage()); 
        }
      
        catch(IOException ioexception)
        {  
            // Other error establishing connection
            System.out.println("Error establishing connection - unexpected exception: " + ioexception.getMessage()); 
        }
        
   }

    private ChatClient(double d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
   public void run()
   {  
       while (thread != null)
       {  
           try
           {  
               // Sends message from console to server
               streamOut.writeUTF(console.readLine());
               streamOut.flush();
           }
         
           catch(IOException ioexception)
           {  
               System.out.println("Error sending string to server: " + ioexception.getMessage());
               stop();
           }
       }
    }
    
    
    public void handle(String msg)
    {  
        // Receives message from server
        if (msg.equals(".quit"))
        {  
            // Leaving, quit command
            System.out.println("Exiting...Please press RETURN to exit ...");
            stop();
        }
        else
            // else, writes message received from server to console
            System.out.println(msg);
    }
    
    // Inits new client thread
    public void start() throws IOException
    {  
        console   = new DataInputStream(System.in);
        streamOut = new DataOutputStream(socket.getOutputStream());
        if (thread == null)
        {  
            client = new ChatClientThread(this, socket);
            thread = new Thread(this);                   
            thread.start();
        }
    }
    
    // Stops client thread
    public void stop()
    {  
        if (thread != null)
        {  
            thread.stop();  
            thread = null;
        }
        try
        {  
            if (console   != null)  console.close();
            if (streamOut != null)  streamOut.close();
            if (socket    != null)  socket.close();
        }
      
        catch(IOException ioe)
        {  
            System.out.println("Error closing thread..."); 
        }
            client.close();  
            client.stop();
        }
   
    
    public static void main(String args[])
    {  
        ChatClient client = null;
            // Calls new client
        client = new ChatClient("localhost", 1234);
    }
    
}

class ChatClientThread extends Thread
{  
    private Socket           socket   = null;
    private ChatClient       client   = null;
    private DataInputStream  streamIn = null;

    public ChatClientThread(ChatClient _client, Socket _socket)
    {  
        client   = _client;
        socket   = _socket;
        open();  
        start();
    }
   
    public void open()
    {  
        try
        {  
            streamIn  = new DataInputStream(socket.getInputStream());
        }
        catch(IOException ioe)
        {  
            System.out.println("Error getting input stream: " + ioe);
            client.stop();
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
                client.stop();
            }
        }
    }
}

