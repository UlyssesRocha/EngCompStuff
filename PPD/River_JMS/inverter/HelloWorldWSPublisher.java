package inverter;

/**
 * Created by ulysses on 4/27/15.
 */
import javax.xml.ws.Endpoint;
public class HelloWorldWSPublisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:9999/WS/HelloWorldWS",new HelloWorldImpl());
    }
}
