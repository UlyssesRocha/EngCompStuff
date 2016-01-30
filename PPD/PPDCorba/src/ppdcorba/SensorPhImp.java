/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppdcorba;
import Sensores.*;

/**
 *
 * @author ulysses
 */
public class SensorPhImp extends PhPOA{
    int currentPh=0;
    public int getPhValue (){
         return currentPh;
     }
     
    public boolean setPhValue (int ph){
      this.currentPh = ph;
      return true;
    }
  
}
