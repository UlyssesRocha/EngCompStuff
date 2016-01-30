/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author ulysses
 */
//http://www.linhadecodigo.com.br/artigo/3401/serializacao-solucao-para-persistencia-de-objetos.aspx#ixzz3RsGNWwGq

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
 
public class Serializador {
 
    public Serializador() {    }
     
    public void serializar(Object obj) throws Exception {
        FileOutputStream fos = new FileOutputStream("data.xPost");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.close();
    }
}


