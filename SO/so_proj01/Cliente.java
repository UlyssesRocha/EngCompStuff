package so_proj01;

import java.util.concurrent.Semaphore;

import java.util.logging.Logger;

public class Cliente extends Thread {
    private final static Logger logger = Logger.getLogger("Log_SO_Proj01_Clientes");
    
    private enum Status{dormindo, emAtendimento, atendido};
    
    private final int senha;
    private final int tempo; //Tempo de atendimento em segundos
    private Status status;
    
    private final Semaphore semaphore;

    public Cliente(int senha, int tempo) {
        this.senha = senha;
        this.tempo = tempo;
        this.status = Status.dormindo;
        
        this.semaphore = new Semaphore(0);
        
        logger.log(Level2.CLIENTE_NOVO, this.toString());
        
        this.start();
    }

    @Override
    public void run() {
        Padaria.zeroClientes.release(); //mexer nisso depois

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        this.status = Status.emAtendimento;
        logger.log(Level2.CLIENTE_EM_ATENDIMENTO, this.toString());
        
        long tempoInicial = System.currentTimeMillis();
        while ( ((int)((System.currentTimeMillis() - tempoInicial)/1000)) < this.tempo );

        this.status = Status.atendido;
        logger.log(Level2.CLIENTE_ATENDIDO, this.toString());
    }

    public void acorda() {
        if(semaphore.availablePermits() == 0)
            semaphore.release();
    }
 
    @Override
    public String toString() {
        return "Cliente_" + this.senha + " : " + this.tempo + " - " + this.status;
    }

    public int getSenha() {
        return senha;
    }

    public int getTempo() {
        return tempo;
    }

    public Status getStatus() {
        return status;
    }
}
