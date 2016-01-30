
package so_proj01;

import java.util.logging.Logger;


public class Vendedor extends Thread {
    private final static Logger logger = Logger.getLogger("Log_SO_Proj01_Vendedores");

    private enum Status { dormindo, atendendo };

    private int id;
    private Cliente cliente;
    private Status status;

    public Vendedor(int id) {
        this.id = id;
        this.status = Status.dormindo;
        
        this.start();
    }

    public void run() {
        while (true) {
            logger.log(Level2.VENDEDOR_AQUARDANDO, "Vendedor_" + this.id + " esperando novo cliente.");
            
            try {
                Padaria.zeroClientes.acquire(); //mexer aki depois
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.status = Status.atendendo;
            this.cliente = Padaria.getProxCliente();
            logger.log(Level2.VENDEDOR_ATENDENDO, this.toString());
            this.cliente.acorda();

            long tempoInicial = System.currentTimeMillis();
            while ( ((int)((System.currentTimeMillis() - tempoInicial)/1000)) < cliente.getTempo() );
            
            this.status = Status.dormindo;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vendedor_");
        sb.append(this.id);
        sb.append(" ");
        sb.append(this.status);

        if (this.status == Status.atendendo) {
            sb.append(" Cliente_");
            sb.append(this.cliente.getSenha());
        }

        return sb.toString();
    }
}
