
package inverter;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "HelloWorldWS", targetNamespace = "http://inverter/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface HelloWorldWS {


    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "helloWorld", targetNamespace = "http://inverter/", className = "inverter.HelloWorld")
    @ResponseWrapper(localName = "helloWorldResponse", targetNamespace = "http://inverter/", className = "inverter.HelloWorldResponse")
    @Action(input = "http://inverter/HelloWorldWS/helloWorldRequest", output = "http://inverter/HelloWorldWS/helloWorldResponse")
    public String helloWorld(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

}
