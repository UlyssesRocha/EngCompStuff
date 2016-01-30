/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.PostIt;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ulysses
 */
public interface  MyInterface extends Remote {
 
  boolean addNewUser(String name, String pass) throws RemoteException;
 // boolean editUserPass(String name, String oldPass,String newPass) throws RemoteException;
  String login(String name, String pass) throws RemoteException;
  
  int addPostIt(String name, String token, String textOfPostIt) throws RemoteException;
  List<PostIt> getAllPostIts(String name, String Token) throws RemoteException;
  boolean editPostIt(String name, String token,int id, String text)throws RemoteException;
  boolean editPostItColor(String name, String token,int id, int color) throws RemoteException;
  boolean removePostIt(String name, String token, int id) throws RemoteException;
  
  PostIt updatePostIt(String name, String token, int id) throws RemoteException;
  
}


