package ppdSocket;

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
    +r Ready for play [+rJoao] //envia nome do adversario
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

import java.net.*;
import java.io.*;

public class Server implements Runnable{
        //Variaveis do chat
        private User clients[] = new User[2];
	private ServerSocket server_socket = null;
	private Thread thread = null;
	private int clientCount = 0;
        //Variaveis do Jogo
        Board playBoard = new Board();
        private int readyClientCount = 0;

	public Server(int port){  
		try{  
                    // Binds to port and starts server
                    System.out.println("Binding to port " + port);
                    server_socket = new ServerSocket(port);  
                    System.out.println("Server started: " + server_socket);
                    start();
        	}
      		catch(IOException ioexception){  
                    // Error binding to port
                    System.out.println("Binding error (port=" + port + "): " + ioexception.getMessage());
                }
    	}
    
        /* INICIO DE CODIGO ORIGINAL - NAO ALTERADO -
        Trechos do arquivo "ChatServer.java", original disponivel na pasta do projeto "CodigosDeApoio".
        Autor de "ChatServer.java" desconhecido.
        */
    	public void run()
    	{  
        	while (thread != null)
        	{  
            		try
            		{  
                		// Adds new thread for new client
                		System.out.println("Waiting for a client ..."); 
                		addThread(server_socket.accept()); 
            		}
            		catch(IOException ioexception)
            		{
                		System.out.println("Accept error: " + ioexception); stop();
            		}
        	}
    	}
    
   	public void start()
    	{  
        	if (thread == null)
        	{  
            		// Starts new thread for client
            		thread = new Thread(this); 
            		thread.start();
        	}
    	}
    
    	public void stop()
    	{  
        	if (thread != null)
        	{
            		// Stops running thread for client
            		thread.stop(); 
            		thread = null;
        	}
    	}
   
    	private int findClient(int ID)
    	{  
        	// Returns client from id
        	for (int i = 0; i < clientCount; i++)
            		if (clients[i].getID() == ID)
                		return i;
        	return -1;
    	}
        
        public synchronized void remove(int ID)
    	{  
        	int pos = findClient(ID);
      
       	 	if (pos >= 0)
        	{  
            		// Removes thread for exiting client
            		User toTerminate = clients[pos];
            		System.out.println("Removing client thread " + ID + " at " + pos);
            		if (pos < clientCount-1)
                		for (int i = pos+1; i < clientCount; i++)
                    			clients[i-1] = clients[i];
            		clientCount--;
         
            		try
            		{  
                		toTerminate.close(); 
            		}
         
            		catch(IOException ioe)
            		{  
                		System.out.println("Error closing thread: " + ioe); 
            		}
         
            		toTerminate.stop(); 
        	}
    	}
    
    	private void addThread(Socket socket)
    	{  
    	    	if (clientCount < clients.length)
        	{  
            		// Adds thread for new accepted client
            		System.out.println("Client accepted: " + socket);
            		clients[clientCount] = new User(this, socket);
           		try
            		{  
                		clients[clientCount].open(); 
                		clients[clientCount].start();  
                		clientCount++; 
            		}
            		catch(IOException ioe)
            		{  
               			System.out.println("Error opening thread: " + ioe); 
            		}
       	 	}
        	else
            		System.out.println("Client refused: maximum " + clients.length + " reached.");
    	}


        /* FINAL DO TRECHO DO CODIGO ORIGINAL*/
        
    
    	public synchronized void handle(int ID, String input){
                //identifica origem da mensagem recebida
                int array_id = findClient(ID);
                System.out.println(input);
                
        	if (input.startsWith("-q")) 
                       quit(array_id, ID);
                else if (input.startsWith("-m"))		
                       message(array_id, input.substring(2));
                else if (input.startsWith("-c")) 
                       connection(array_id,input.substring(2));
                else if (input.startsWith("-p"))
                       playedCard(array_id,input.substring(2));
                else
                    error(array_id);
	}
        
        //conect + nome, se os 2 jogadores ja estiverem na sala, iniciar jogo
        private void connection(int array_id, String input){
            clients[array_id].setNameOfPlayer(input);
            clients[array_id].send("+a");
            readyClientCount++;
            System.out.println(clientCount);
            if(readyClientCount==2)
                startGame();
        }
        //envia pronto e nome do arversario
        private void readyForPlay(){
            for (int i = 0; i < clientCount; i++)
                clients[i].send("+r"+clients[(i+1)%2].getNameOfPlayer());
        }
        
        //Mensage - Mensagem enviada por um jogador para todos os outros
        private void message(int array_id, String input){
             // Brodcast message for every other client online
             for (int i = 0; i < clientCount; i++)
                   clients[i].send("+m"+clients[array_id].getNameOfPlayer()+": "+ input);   
        }
        
        //Informa cliente que é sua vez de jogar
        private void yourTurn(int playerArray_id){
            clients[playerArray_id].startNewRound();
            clients[playerArray_id].send("+y");
        }
        
        //troca jogador
        private void changePlayer(int lastPlayerArray_id){
            //informa que o movimento nao resultou em sucesso. *FALHA*
            clients[lastPlayerArray_id].send("+f");
            //finaliza round
             clients[lastPlayerArray_id].endRound();
             //inicia turno do proximo jogador (para 2 jogadores)
             yourTurn((++lastPlayerArray_id)%2);
        }
        
        //pontua jogador, solicita nova jogada e informa adversarios
        private void playerScore(int playerArray_id){
            //adiciona pontos do jogador no sistema
            clients[playerArray_id].addPoints();
            //informa jogador 
            clients[playerArray_id].send("+s"+clients[playerArray_id].getNumberOfPoints());
            //informa adversarios
            for (int i = 0; i < clientCount; i++)
                if(i!=playerArray_id)
                    clients[i].send("+o"+clients[playerArray_id].getNumberOfPoints());
            //solicita nova jogada.
            yourTurn(playerArray_id);
        }
        
        //Erro, mensagem desconhecida/corrompida/inapropriada
        private void error(int array_id){
            clients[array_id].send("+e"); 
        }
        
        //movimento ilegal *carta ja virada ou nao existente* o jogador devera jogar novamente 
        private void illegalMove(int array_id){
            clients[array_id].send("+i"); 
        }
        
        //Fail, success
        private void movementResult(int playerArray_id){
            if(playBoard.isThisRoundSuccessful()){
                playerScore(playerArray_id);
                if(playBoard.numberOfCardsLeft()==0)
                    endGame();
            }
            else
                changePlayer(playerArray_id);
        }
        
        //verifica ganhador (MELHORAR ESSE CODIGO!!!)
        private void endGame(){
            if(clients[0].getNumberOfPoints()>clients[1].getNumberOfPoints()){
                clients[0].send("+w");
                clients[1].send("+l");
            }
            else if(clients[1].getNumberOfPoints()>clients[0].getNumberOfPoints()){
                clients[1].send("+w");
                clients[0].send("+l");
            }
            else{
                clients[0].send("+d");
                clients[1].send("+d");
            }          
        }
        
        //informa a todos o conteudo da carta revelada
        private void playedCardBroadcast(int cardNumber){
            int contentOfTheCard = playBoard.flipCardAtPosition(cardNumber);
            for (int i = 0; i < clientCount; i++)
                clients[i].send("+p"+(cardNumber+1)+","+contentOfTheCard);//+1 para ajuste do vetor 
        }
       
        //Trata carta jogada
        private void playedCard(int playerArray_id,String input){
            //se o jogador estiver em seu turno
            if(clients[playerArray_id].isPlaying()){
                //Captura numero da carta a ser revelada, -1 para ajustar vetor iniciando em 0
                int movedCard = Integer.parseInt(input)-1;
                //verifica se a carta selecionada e valida e disponivel
                if(movedCard>=0 && movedCard<24 && playBoard.isThisCardAvaliable(movedCard)){
                    //move carta no tabuleiro e informa jogadores.
                    playedCardBroadcast(movedCard);
                    //primeira carta do turno do jogador
                    if(clients[playerArray_id].getNumberOfMovesThisRound()==0)              
                        clients[playerArray_id].playMove(movedCard);
                    else{
                        clients[playerArray_id].playMove(movedCard);//segunda carta
                        movementResult(playerArray_id);//verifica se houve sucesso
                    }
                }
                else
                    illegalMove(playerArray_id); //movimento ilegal, jogar novamente.       
            }
            else{
                error(playerArray_id); //erro, informa usuario.
            }

        }
        
        //Inicia partida MELHORAR CODIGO ADD random
        private void startGame(){
            readyForPlay();
            yourTurn(0);
        }
        
        //Quit - Saida de um jogador
        private void quit(int array_id, int ID){
            // Client exits
            clients[array_id].send("+bye");
            // Notify remaing users
            for (int i = 0; i < clientCount; i++)
                    if (i!=array_id)
                        clients[i].send("+q"+clients[array_id].getNameOfPlayer());
            remove(ID);
        }
  
}

