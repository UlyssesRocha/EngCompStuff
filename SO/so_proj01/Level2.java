
package so_proj01;

import java.util.logging.Level;


public class Level2 extends Level{
    public static final Level2 CLIENTE_NOVO = new Level2("CLIENTE_NOVO", 999);
    public static final Level2 CLIENTE_EM_ATENDIMENTO = new Level2("CLIENTE_EM_ATENDIMENTO", 998);
    public static final Level2 CLIENTE_ATENDIDO = new Level2("CLIENTE_ATENDIDO", 997);
    
    public static final Level2 VENDEDOR_AQUARDANDO = new Level2("VENDEDOR_AQUARDANDO", 996);
    public static final Level2 VENDEDOR_ATENDENDO = new Level2("VENDEDOR_ATENDENDO", 995);
    

    private Level2(String name, int level) {
        super(name, level);
    }
}
