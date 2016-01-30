package inverter;

/**
 * Created by ulysses on 4/27/15.
 */
import javax.jms.*;
import javax.jws.WebService;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

@WebService(endpointInterface="inverter.HelloWorldWS")
public class HelloWorldImpl implements HelloWorldWS {

    public String helloWorld(String name) {
        System.out.println("usario "+name+" Deve ser moderado");
        try{
            Hashtable properties = new Hashtable();
            properties.put(Context.INITIAL_CONTEXT_FACTORY,"org.exolab.jms.jndi.InitialContextFactory");
            properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");

            Context context = new InitialContext(properties);

            TopicConnectionFactory tfactory = (TopicConnectionFactory) context.lookup("ConnectionFactory");

            TopicConnection tconnection = tfactory.createTopicConnection();
            TopicSession tsession = tconnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            TextMessage message = tsession.createTextMessage();
            message.setText("usario "+name+" Deve ser moderado");

            Topic dest = (Topic) context.lookup("topic1");
            TopicPublisher publisher = tsession.createPublisher(dest);
            publisher.publish(message);

            context.close();
            tconnection.close();


        }catch(Exception e){
            e.printStackTrace();
        }

        return "Usuario sera avaliado "+name;
    }

}
