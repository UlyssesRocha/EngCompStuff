/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.security.SecureRandom;

/**
 *
 * @author ulysses
 */
public class PostIt implements Serializable {
   private SecureRandom random;
    private String postItText;
    private final int postItID;
    private int color;
    
    public PostIt(int id){
        postItID = id;
    }
    
    public String getText(){
        return this.postItText;
    }
    
    public int getId(){
        return postItID;
    }
    
    public void updateString(String newText){
        this.postItText = newText;
    }
    
    public int getColor(){
        return color;
    }
    
    public void setColor(int color){
        this.color = color;
    }
    
}
