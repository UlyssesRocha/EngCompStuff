package Sensores;

/**
* Sensores/TemperaturaHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from proj.idl
* Monday, March 30, 2015 1:23:17 AM BRT
*/

public final class TemperaturaHolder implements org.omg.CORBA.portable.Streamable
{
  public Sensores.Temperatura value = null;

  public TemperaturaHolder ()
  {
  }

  public TemperaturaHolder (Sensores.Temperatura initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = Sensores.TemperaturaHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    Sensores.TemperaturaHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return Sensores.TemperaturaHelper.type ();
  }

}