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
public class SensorCorImp extends CorPOA{
    int currentColor=0;
    public int getCorValue (){
        return currentColor;
    }
    
    public boolean setCorValue (int cor){
        this.currentColor = cor;
        return true;
    }
    
}
