package  jms;
import java.io.*;
import java.util.*;
 
import javax.jms.*;
import javax.jms.Message;
import javax.naming.*;
import javax.jms.MessageListener;

public class SubscriberTopic implements MessageListener {
     
     public static void main(String args[]){
	new SubscriberTopic().Go();
     }

     public void Go(){
     
         try{

		Hashtable properties = new Hashtable();
		properties.put(Context.INITIAL_CONTEXT_FACTORY,"org.exolab.jms.jndi.InitialContextFactory");
		properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");

		Context context = new InitialContext(properties);

		TopicConnectionFactory tfactory = (TopicConnectionFactory) context.lookup("ConnectionFactory");

		TopicConnection tconnection = tfactory.createTopicConnection();
		TopicSession tsession = tconnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

		tconnection.start();

	        Topic dest = (Topic)context.lookup("topic1");
        	TopicSubscriber tsubscriber = tsession.createSubscriber(dest);

		tsubscriber.setMessageListener(this);
         
                        	
			 
         }catch(Exception e){
             e.printStackTrace();
         }       
     }

     public void onMessage(Message message){
         if(message instanceof TextMessage){
             try{
                 System.out.println( ((TextMessage)message).getText());
             }catch(Exception e){
             }
         }
     }

}
