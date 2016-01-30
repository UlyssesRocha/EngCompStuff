/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewClient;

import Model.MyInterface;
import Model.PostIt;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author ulysses
 */
class Client extends Thread{
    Registry r;
    MyInterface service;
    String userName;
    String token;
    private List<PostItView> postItsViews;
    int delay = 600000; //10 minutos
    
    ActionListener taskPerformer = new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
            for(PostItView postIt : postItsViews){
              postIt.reload();
        }
      }
   };

    public Client(String Server,String name, String token) throws RemoteException, NotBoundException{
        r = LocateRegistry.getRegistry(Server, 1099);
        service = (MyInterface)r.lookup("service");
        this.userName = name;
        this.token = token;
        new Timer(delay, taskPerformer).start();
    }
    
    @Override
    public void run(){
        try {
            loadPostIts();
            new Control(this);
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void loadPostIts() throws RemoteException, NotBoundException{
        
        this.postItsViews = new ArrayList<>();
        List<PostIt> postIts = service.getAllPostIts(userName, token);
        
        if(!(postIts.size()>0)){
           service.addPostIt(userName, token, " ");
        }
        
        for(PostIt postIt : postIts){
            postItsViews.add(new PostItView(this, userName, token, postIt.getId(),postIt.getText()) );
            System.out.println(postIt.getText());
        }
    }
    
    public int addPostIt()throws RemoteException, NotBoundException{
        int aux = service.addPostIt(userName, token, " ");
        postItsViews.add(new PostItView(this, userName, token, aux," "));
        return aux;
    }
    
    public void reload() throws RemoteException, NotBoundException{
        System.out.println(postItsViews.size());
        
        for (int i=0;i<=postItsViews.size();i++) {
            postItsViews.get(0).closeRemote();
            postItsViews.remove(0);
        }
//nao sei pq precisa desse outro remove...mas so funciona com isso ¯\_(ツ)_/¯ 
        postItsViews.get(0).closeRemote();
        postItsViews.remove(0);
        
        System.out.println(postItsViews.size());

        loadPostIts();
    }
    
    public void removeView(PostItView viewToRemove){
        postItsViews.remove(viewToRemove);
    }
      
  }