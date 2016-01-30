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
//http://www.linhadecodigo.com.br/artigo/3401/serializacao-solucao-para-persistencia-de-objetos.aspx#ixzz3RsGW1VmT

import java.io.FileInputStream;
import java.io.ObjectInputStream;
 
public class Deserializador {
 
    public Deserializador() {    }
     
    public Object deserializar() throws Exception {
        FileInputStream fis = new FileInputStream("data.xPost");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object o = ois.readObject();
        ois.close();
        return o;
    }
}


