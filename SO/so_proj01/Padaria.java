package so_proj01;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Padaria extends Thread {
    private static final Logger logger = Logger.getLogger("Log_SO_Proj01");
    
    private static Semaphore mutex;
    public static Semaphore zeroClientes;

    private static ArrayList<Vendedor> listaDeVendedores = new ArrayList<Vendedor>();
    private static ArrayList<Cliente> listaDeClientes = new ArrayList<Cliente>();

    private static int senhaAtual = 0; //Senha atendimento
    private static int senhaUltimo = 0; //Senha do ultimo cliente
    private static int numVendedores = 1;

    public static void main(String[] args) throws InterruptedException {
        logger.setLevel(Level.ALL);
       // ConsoleHandler ch = new ConsoleHandler();
       // ch.setFormatter(new SimpleFormatter());
       // logger.addHandler(ch);
        
        inicializaSemafaros();
        criaVendedores();
        testeClientes();
    }

    private static void testeClientes() throws InterruptedException {
        Random r = new Random(System.currentTimeMillis());
        
        for (int i = 0; i < 5; i++) {
            sleep(r.nextInt(5000));
            criaCliente(r.nextInt(5));
        }
    }

    private static void inicializaSemafaros() {
        System.out.println("Padaria: Preparando semafaros ( Aquecendo o forno! )...");
        mutex = new Semaphore(1);
        zeroClientes = new Semaphore(0);
    }

    public static Cliente getProxCliente() {
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

   //INICIO REGIAO CRITICA
        if (senhaUltimo == 0 || senhaAtual >= senhaUltimo) {
            mutex.release();
            return null;
        }
        Cliente proximo = listaDeClientes.get(senhaAtual);
        senhaAtual++;
	//FIM REGIAO CRITICA	

        mutex.release();
        return proximo;
    }

    private static void criaCliente(int tempo) {
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //INICIO REGIAO CRITICA		
        Cliente novoCliente = new Cliente(senhaUltimo, tempo);
        listaDeClientes.add(novoCliente);
        senhaUltimo++;
        //FIM REGIAO CRITICA
        mutex.release();
    }

    private static void criaVendedores() {
        for (int i = 0; i < numVendedores; i++) {
            Vendedor novoVendedor = new Vendedor(i);
            listaDeVendedores.add(novoVendedor);
        }
        
        //logger.log(Level2.NOVO_VENDEDOR, Integer.toString(numVendedores));
    }

}
