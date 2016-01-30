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
public class SensorTempImp extends TemperaturaPOA{
    int currentTemp=0;
    public int getTempValue (){
        return this.currentTemp;
     }
     
    public boolean setTempValue (int temp){
      this.currentTemp = temp;
      return true;
    }
}
