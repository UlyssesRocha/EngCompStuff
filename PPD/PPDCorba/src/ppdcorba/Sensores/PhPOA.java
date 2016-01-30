package Sensores;


/**
* Sensores/PhPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from proj.idl
* Monday, March 30, 2015 1:23:17 AM BRT
*/

public abstract class PhPOA extends org.omg.PortableServer.Servant
 implements Sensores.PhOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("getPhValue", new java.lang.Integer (0));
    _methods.put ("setPhValue", new java.lang.Integer (1));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // Sensores/Ph/getPhValue
       {
         int $result = (int)0;
         $result = this.getPhValue ();
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 1:  // Sensores/Ph/setPhValue
       {
         int ph = in.read_long ();
         boolean $result = false;
         $result = this.setPhValue (ph);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:Sensores/Ph:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Ph _this() 
  {
    return PhHelper.narrow(
    super._this_object());
  }

  public Ph _this(org.omg.CORBA.ORB orb) 
  {
    return PhHelper.narrow(
    super._this_object(orb));
  }


} // class PhPOA