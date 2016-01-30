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


public class Client{  
    private Socket socket              = null;
    private ClientThread client    = null;
    private mainGameView gameView = null;
    private String clientName = null;
    
    public Client(String serverName, int serverPort,mainGameView gameView){  
        System.out.println("Establishing connection to server...");
        this.gameView = gameView;
        
        try{
            // Establishes connection with server (name and port)
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected to server: " + socket);
    
            
            client = new ClientThread(this, socket);
        }
        
        catch(UnknownHostException uhe){  
            // Host unkwnown
            System.out.println("Error establishing connection - host unknown: " + uhe.getMessage()); 
        }
      
        catch(IOException ioexception){  
            // Other error establishing connection
            System.out.println("Error establishing connection - unexpected exception: " + ioexception.getMessage()); 
        }
        
   }
        
    public void handle(String msg){  
        //+m Message+nameOfSource: +Content [+mJoao da Silva: Olá, tudo bem?]//broadcast
        System.out.println(msg);
        
        if (msg.startsWith("+m")){
            gameView.receiveMessage(msg.substring(2));
        }
        //+p Played+card+,+content[+p02,08] //Broadcast
        else if (msg.startsWith("+p")){
            int card,content;
            //conteudo antes da ,
            card=Integer.parseInt(msg.substring(2).split(",")[0]);
            //cpnteudo depois da ,
            content=Integer.parseInt(msg.substring(2).split(",")[1]);
            //envia para a view
            gameView.receiveCard(card, content);
        }
        //+y YourTurn [+y]
        else if (msg.startsWith("+y")){
            gameView.hideCards("Sua Vez de Jogar",true);
        }
        //+f FailMove [+f] 
        else if (msg.startsWith("+f")){
            gameView.hideCards("Oponente Jogando",false);
       }
        
        //+s SuccessMove+TotalPoints [+s9] (cards removed)
        else if (msg.startsWith("+s")){
            gameView.removeCards("Acertou! Jogue novamente",true);
      }
        //+o OpponentSucessMove+Points [+o9] (cards removed)
        else if (msg.startsWith("+o")){
            gameView.removeCards("Ponto! Oponente Jogando",false);
        }
        
        //+r Ready for play [+r+NomeDoOponente] //recebo o nome do oponente aqui!
        else if (msg.startsWith("+r")){
            gameView.addOpponentName(msg.substring(2));
        }
        
        
        /*    +l Lose (voce perdeu)
              +w Won (voce ganhou)
              +d Draw (empate) */
        //Tratar Final 
        else if (msg.startsWith("+l")|| msg.startsWith("+w")||msg.startsWith("+d") ){
            if(msg.startsWith("+l"))
                gameView.changeStatus("Você Perdeu!");
            else if(msg.startsWith("+w"))
                gameView.changeStatus("Você Ganhou!");
            else if(msg.startsWith("+d"))
                gameView.changeStatus("Você Empatou!");
        }
    }
    
/* Implementando 4 mensagens do client utilizados pela view*/    
    public void connectAndSendName(String name){
       this.clientName = name; 
       client.send("-c"+name);
    }
    
    public void sendMessage(String message){
        client.send("-m"+message);
    }
    
    public void quitConnection(){
        client.send("-q");
        closeClient();
    }
    
    public void playTheCard(int cardNumber){
        client.send("-p"+cardNumber);
    }
/* Implementado as 4 mensagens do client*/    

    public void closeClient(){
        try{  
            if (socket    != null)  socket.close();
        }  
        catch(IOException ioe)
        {  
            System.out.println("Error closing thread..."); 
        }
    }
}


