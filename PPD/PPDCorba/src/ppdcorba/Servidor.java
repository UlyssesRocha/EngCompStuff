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
public class Servidor {
    public static void main(String args[]){
        try{
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objPoa = orb.resolve_initial_references("RootPOA");
            POA rootPOA = POAHelper.narrow(objPoa);
            org.omg.CORBA.Object obj = orb.resolve_initial_references("NameService") ;
            NamingContext naming = NamingContextHelper.narrow(obj);
            
            //=============================Criando servidores
            SensorCorImp sensCor = new SensorCorImp();
            SensorPhImp sensPh = new SensorPhImp();
            SensorTempImp sensTemp = new SensorTempImp();
            
            //=============================Criando referencias
            org.omg.CORBA.Object objRef1 = rootPOA.servant_to_reference(sensCor);
            org.omg.CORBA.Object objRef2 = rootPOA.servant_to_reference(sensPh);
            org.omg.CORBA.Object objRef3 = rootPOA.servant_to_reference(sensTemp);
            
            //=============================nomes
            NameComponent[] name1 = {new NameComponent("Cor","Exemplo")};
            NameComponent[] name2 = {new NameComponent("Ph","Exemplo")};
            NameComponent[] name3 = {new NameComponent("Temperatura","Exemplo")};
            
            //============================Binds
            naming.rebind(name1,objRef1);
            naming.rebind(name2,objRef2);
            naming.rebind(name3,objRef3);
            
            //=========================== 
            rootPOA.the_POAManager().activate();
            System.out.println("Servidor Pronto."); orb.run();
        }
        catch (Exception ex){ System.out.println("Erro"); 
            ex.printStackTrace();}
        } 
}
