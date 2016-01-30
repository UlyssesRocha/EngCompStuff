package ppdSocket;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author ulysses
 */
public class Board {
    private boolean [] isThisCardAvaliable;
    private int [] contentOfTheCard;
    private int []lastFlipedCards;
    private int cardsInGame;
    private int roundMovesCounter;
    Random rand = new Random();

    public Board(){
        this.isThisCardAvaliable = new boolean[24];
        this.contentOfTheCard = new int[24];
        this.lastFlipedCards= new int[2];
        this.cardsInGame = 24;
        this.roundMovesCounter = 0;
        shuffleCards();
        for(int i=0;i<24;i++)
            System.out.println("Conteudo carta - "+i+" : "+contentOfTheCard[i]);
    }
    //Distribui cartas aleatoriamente no tabuleiro
    private void shuffleCards(){
        //Criando arrays dinamico para embaralhar as cartas em tempo constante
        ArrayList<Integer> auxDeck = new ArrayList<Integer>();
        //adicionando um par de cada carta no deck1, valores de 1 a 12
        for(int i=1;i<=12;i++){
           auxDeck.add(i);
           auxDeck.add(i);
        }
        for(int i=0;i<24;i++){
            this.isThisCardAvaliable[i] = true;
            //Pega uma carta aleatoria do deck auxiliar e adiciona ao deck principal
            int next = rand.nextInt(auxDeck.size());
            this.contentOfTheCard[i]= auxDeck.get(next);
            auxDeck.remove(next);
        }
    }
    //Revela carta em uma posicao dado o numero da carta
    public int flipCardAtPosition(int cardNumber){
        //se a carta estiver disponivel e ainda houverem movimentos validos
        if(this.isThisCardAvaliable[cardNumber] && roundMovesCounter<2){
            //torna a carta indisponivel
            this.isThisCardAvaliable[cardNumber] = false;
            //incrementa numero de movimentos do round e armazena carta virada
            this.lastFlipedCards[this.roundMovesCounter++]=cardNumber;
            //retorna valor da carta
            return this.contentOfTheCard[cardNumber];
        }
        else
            //erro
            return -1;
    }
    public int numberOfCardsLeft(){
        return this.cardsInGame;
    }
    public boolean isThisCardAvaliable(int cardNumber){
        return this.isThisCardAvaliable[cardNumber];
    }
    //verifica se houve sucesso no round 
    public boolean isThisRoundSuccessful(){
        //zera numero de movimentos na rodada
        this.roundMovesCounter=0;
        //se as cartas forem iguais, retorne sucesso
        if(this.contentOfTheCard[this.lastFlipedCards[0]]==this.contentOfTheCard[this.lastFlipedCards[1]]){
            //retira 2 cartas do contador de cartas em jogo
            this.cardsInGame-=2;
            return true;
        }
        else{
            //se nao, torne as cartas disponiveis para a nova rodada
            this.isThisCardAvaliable[this.lastFlipedCards[0]] = this.isThisCardAvaliable[this.lastFlipedCards[1]] = true;
            return false;
        }
  }
    
}
