/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Model.MyInterface;
import Model.User;
import Model.PostIt;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ulysses
 */
class Service extends UnicastRemoteObject implements MyInterface {
    
    List<User> users; //deverá se tornar um novo objeto
    Serializador save = new Serializador();
    Deserializador load = new Deserializador();
    
    public Service() throws RemoteException {
        try {
            users = (List<User>) load.deserializar();
         } catch (Exception ex) {
             System.out.println("Criando lista"+ex);  
             users = new ArrayList<>();
         }
      System.out.println("Servidor criado!");  
   }
    
   public static void main(String arg[]) throws RemoteException {
      Registry r = LocateRegistry.createRegistry(1099);
      r.rebind("service", new Service());
   }

    @Override
    public boolean addNewUser(String name, String pass) throws RemoteException {
        for (User user : users) {
            if(user.getUserName().equals(name))
                return false;
        }
        users.add( new User(name,pass) );
        System.out.println("Usuario "+name+" criado! Senha "+pass);  
        saveData();
        return true;
    }

    @Override
    public String login(String name, String pass) throws RemoteException {
        System.out.println("Usuario Solicitando login"+name+" criado! Senha "+pass);  
        //Pesquisa usuarios e retorna token de acesso caso o login / senha esteja correto
        for (User user : users) {
            if (user.getUserName().equals(name) && user.checkTheUserPass(pass)) {
                return user.getUserToken();
            }
        }
        saveData();
        return null;
    }

      
    @Override
    public int addPostIt(String name, String Token, String textOfPostIt) throws RemoteException {
        //usuario adiciona postIt
        for (User user : users) {
            if (user.getUserName().equals(name)) {
                int aux = user.addPostIt(Token, textOfPostIt);
                saveData();
                return aux;
            }
        }
        return 0;
    }

    
    @Override
    public List<PostIt> getAllPostIts(String name, String Token) throws RemoteException {
        //retorna todos os postits do usuario
        for (User user : users) {
            if (user.getUserName().equals(name)) {
                return user.getAllPostIt(Token);
            }
        }
        return null;
    }
    

    @Override
    public boolean editPostIt(String name, String Token, int id, String text) throws RemoteException {
        for (User user : users) {
            if (user.getUserName().equals(name)) {
                 user.editPostIt(Token, id, text);
                 saveData();
                 return true;
            }
        }
        return false;
    }
    
    private void saveData(){
        try {
            save.serializar(users);
        } catch (Exception ex) {
            System.out.println("Erro ao persistir dados!"+ex);  
        }
    }

    @Override
    public boolean removePostIt(String name, String token, int id) throws RemoteException {
         for (User user : users) {
            if (user.getUserName().equals(name)) {
                boolean aux = user.removePostIt(token, id);
                saveData();
                return aux;
            }
        }
        return false;
    }

    @Override
    public PostIt updatePostIt(String name, String token, int id) throws RemoteException {
           for (User user : users) {
              if (user.getUserName().equals(name)) {
                  return user.getPostIt(token, id);
              }
           }
           return null;
    }

    @Override
    public boolean editPostItColor(String name, String Token, int id, int color) throws RemoteException {
        for (User user : users) {
          if (user.getUserName().equals(name)) {
              user.editPostItColor(Token, id, color);
              saveData();
              return true;
          }
        }
        return false;
    }
}
