/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppdcorba;

/**
 *
 * @author ulysses
 */
import Sensores.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
public class Cliente {
     public static void main(String args[]){
    try {
        ORB orb = ORB.init(args,null);
        org.omg.CORBA.Object obj = orb.resolve_initial_references("NameService") ;
        NamingContext naming = NamingContextHelper.narrow(obj);
        NameComponent[] name = {new NameComponent("Cor","Exemplo")};
        org.omg.CORBA.Object objRef = naming.resolve(name);
        Cor calc = CorHelper.narrow(objRef);
       System.out.println("SETANDO cincao");
        calc.setCorValue(19);
        System.out.println("COR ="+ calc.getCorValue());
        
    }
    catch (Exception e) { 
        System.out.println("ERROR : " + e) ;
        e.printStackTrace(System.out);
    }
  }
}
