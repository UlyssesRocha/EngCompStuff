/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.PostIt;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ulysses
 */
public class User implements Serializable{       
   private SecureRandom random;
   private String userName;
   private String userPass;
   private String userToken;
   private int idCounter;
   List<PostIt> postIts; //deverá se tornar um novo objeto

   public User(String userName, String userPass) {
       this.userName = userName;
       this.userPass = userPass;
       this.userToken = null;
       postIts = new ArrayList<>();
       idCounter = 0;
   }
   
   public int addPostIt(String token, String text){
       if(!checkTheUserToken(token))
           return 0;
       idCounter++;
       PostIt newPostIt = new PostIt(idCounter);
       newPostIt.updateString(text);
       postIts.add(newPostIt);
       return idCounter; 
   }
   
   public boolean removePostIt(String token, int id){
        if(!checkTheUserToken(token))
           return false; 
       for(PostIt postIt : postIts){
           if(postIt.getId() == id){
               postIts.remove(postIt);
               return true;
           }
       }
       return false;
   }
   
   public PostIt getPostIt(String token, int id){
        if(!checkTheUserToken(token))
           return null; 
       for(PostIt postIt : postIts){
           if(postIt.getId() == id){
               return postIt;
               
           }
       }
       return null;
   }
   
 public boolean editPostItColor(String token, int id, int color){
       if(!checkTheUserToken(token))
           return false; 
       for(PostIt postIt : postIts){
          if( postIt.getId() == id){
              postIt.setColor(color);
              return true;
          }
       } 
       return false;
   }   
   public List<PostIt> getAllPostIt(String token){
       if(!checkTheUserToken(token))
           return null;
       return postIts;
   }
   

   public int getNumberOfPostIts(String token){
       if(!checkTheUserToken(token))
           return 0;   
       return postIts.size();
   }
   
   public String getUserName(){
       return userName;
   }
   
   public boolean editPostIt(String token, int id, String text){
       if(!checkTheUserToken(token))
           return false; 
       for(PostIt postIt : postIts){
          if( postIt.getId() == id){
              postIt.updateString(text);
              return true;
          }
       } 
       return false;
   }
   
   public boolean checkTheUserPass(String pass){
       return pass.equals(userPass);
   }
   
   public String getUserToken(){
       if(userToken==null){
          generateNewToken();
       }
       return this.userToken;
   }
   
   private boolean checkTheUserToken(String token){
       return userToken.equals(token);
   }
   
   private void generateNewToken(){
       random = new SecureRandom();
       userToken = new BigInteger(130, random).toString(32);
   }
}