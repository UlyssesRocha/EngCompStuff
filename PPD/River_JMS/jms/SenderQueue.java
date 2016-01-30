package  jms;

import java.io.*;
import java.util.*;
import javax.jms.*;
import javax.naming.*;

public class SenderQueue{
    
    public static void main(String args[]){

     try{

		Hashtable properties = new Hashtable();
		properties.put(Context.INITIAL_CONTEXT_FACTORY,"org.exolab.jms.jndi.InitialContextFactory");
		properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");

		Context context = new InitialContext(properties);

		QueueConnectionFactory qfactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");

		QueueConnection qconnection = qfactory.createQueueConnection();
		QueueSession qsession = qconnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

		TextMessage message = qsession.createTextMessage();
		message.setText("Mensagem do Sender");
      
		javax.jms.Queue dest = (javax.jms.Queue) context.lookup("queue1");
		QueueSender sender = qsession.createSender(dest);
		sender.send(message);
        
		context.close();
		qconnection.close();

        
    }catch(Exception e){
      e.printStackTrace();
    }
 }
}
