package  jms;

import java.io.*;
import java.util.*;
import javax.jms.*;
import javax.naming.*;

public class PublisherTopic{
    
    public static void main(String args[]){

     try{

		Hashtable properties = new Hashtable();
		properties.put(Context.INITIAL_CONTEXT_FACTORY,"org.exolab.jms.jndi.InitialContextFactory");
		properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");

		Context context = new InitialContext(properties);

		TopicConnectionFactory tfactory = (TopicConnectionFactory) context.lookup("ConnectionFactory");

		TopicConnection tconnection = tfactory.createTopicConnection();
		TopicSession tsession = tconnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

		TextMessage message = tsession.createTextMessage();
		message.setText("Mensagem do Sender");
      
		Topic dest = (Topic) context.lookup("topic1");
		TopicPublisher publisher = tsession.createPublisher(dest);
		publisher.publish(message);
        
		context.close();
		tconnection.close();

        
    }catch(Exception e){
      e.printStackTrace();
    }
 }
}
