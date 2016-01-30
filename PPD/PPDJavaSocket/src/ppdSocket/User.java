/*
IFCE - Programação Paralela e Distribuida"
Projeto "Jogo da memoria com chat utilizando sockets
12/01/2015
Ulysses Rocha 201027020070
ulysses@lit.ifce.edu.br

Arquvio "ChatServer.java" utilizado como base para o desenvolvimento.
Autor de "ChatServer.java" desconhecido.
Códigos de apoio utilizados disponiveis na pasta do projeto "CodigosDeApoio".

Fontes externas "de pesquisa:

--Mensagens--
    Client->Server
    -c Connect+name [-cJoao da Silva] 
    -q Quit [-q]
    -m Message+content[-mOlá, tudo bem?]
    -p Play+cardNumber [-p02] //01 a 24

    Server->Client
    +a AcceptedConnection [+a]
    +r Ready for play [+r] //Broadcast
    +m Message+nameOfSource: +Content [+mJoao da Silva: Olá, tudo bem?] //broadcast
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class User extends Thread{  
    //Variaveis de controle da conexao
    private Server       server    = null;
    private Socket           socket    = null;
    private int              ID        = -1;
    private DataInputStream  streamIn  =  null;
    private DataOutputStream streamOut = null;
    
    //Variaveis do jogo
    private String nameOfPlayer = null;
    private int numberOfPoints = 0;
    private boolean isPlaying = false;
    private int numberOfMovesThisRound = 0;
    private int[] movesThisRound = new int[2];
    
    public User(Server _server, Socket _socket){  
        super();
        server = _server;
        socket = _socket;
        ID     = socket.getPort();
    }
    
    // Sends message to client
    public void send(String msg){   
        try
        {  
            streamOut.writeUTF(msg);
            streamOut.flush();
        }
       
        catch(IOException ioexception)
        {  
            System.out.println(ID + " ERROR sending message: " + ioexception.getMessage());
            server.remove(ID);
            stop();
        }
    }
    
    // Runs thread
    public void run()
    {  
        System.out.println("Server Thread " + ID + " running.");
      
        while (true)
        {  
            try
            {  
                server.handle(ID, streamIn.readUTF());
            }
         
            catch(IOException ioe)
            {  
                System.out.println(ID + " ERROR reading: " + ioe.getMessage());
                server.remove(ID);
                stop();
            }
        }
    }
    
    // Opens thread
    public void open() throws IOException
    {  
        streamIn = new DataInputStream(new 
                        BufferedInputStream(socket.getInputStream()));

        streamOut = new DataOutputStream(new
                        BufferedOutputStream(socket.getOutputStream()));
    }
    
    // Closes thread
    public void close() throws IOException
    {  
        if (socket != null)    socket.close();
        if (streamIn != null)  streamIn.close();
        if (streamOut != null) streamOut.close();
    }
    
    
    
    public int getID(){  
        return ID;
    }
    
    public String getNameOfPlayer(){
        return this.nameOfPlayer;
    }
    
    public int getNumberOfMovesThisRound(){
        return this.numberOfMovesThisRound;
    }
    
    public boolean isPlaying(){
        return this.isPlaying;
    }

    public int getNumberOfPoints(){
        return numberOfPoints;
    }
    
    public void addPoints(){
        numberOfPoints++;
    }
    
    public void setNameOfPlayer(String name){
        this.nameOfPlayer = name;
    }
    
    public void startNewRound(){
        this.isPlaying = true;
        this.numberOfMovesThisRound=0;
    }
    
    public void endRound(){
        this.isPlaying = false;
    }
    
    public void playMove(int numberOfMovedCard){
        this.movesThisRound[this.numberOfMovesThisRound++]= numberOfMovedCard;
    }
    
    public int[] cardPositionPlayedThisRound(){
        return this.movesThisRound;
    }
    
}
