/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 * 
 * @author JHelp
 */
package jhelp.security.pulicPrivateKey;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import jhelp.util.xml.DynamicWriteXML;
import jhelp.xml.ExceptionXML;
import jhelp.xml.InvalidParameterValueException;
import jhelp.xml.MarkupXML;
import jhelp.xml.MissingRequiredParameterException;
import jhelp.xml.ParseXMLlistener;

/**
 * Public key to distribute<br>
 * <br>
 * Last modification : 8 juil. 2010<br>
 * Version 0.0.0<br>
 * 
 * @author JHelp
 */
public interface JHelpPublicKey
{
   /**
    * Deserialize the key from a stream
    * 
    * @param inputStream
    *           Stream to read
    * @throws IOException
    *            On deserialization problem
    */
   public void deserialize(InputStream inputStream) throws IOException;

   /**
    * Encrypt a stream
    * 
    * @param inputStream
    *           The stream to encrypt
    * @param outputStream
    *           Stream to write the ciphers
    * @throws IOException
    *            If an IO problem append
    */
   public void encrypt(InputStream inputStream, OutputStream outputStream) throws IOException;

   /**
    * Deserialze for XML
    * 
    * @param markupXML
    *           Markup to parse
    * @throws ExceptionXML
    *            If markup is not a valid XML representation
    */
   public void fromXML(MarkupXML markupXML) throws ExceptionXML;

   /**
    * Serialize the key to a stream
    * 
    * @param outputStream
    *           Stream where write
    * @throws IOException
    *            A serialization problem
    */
   public void serialize(OutputStream outputStream) throws IOException;

   /**
    * Deserialize from a XML stream.<br>
    * Can be call by {@link ParseXMLlistener#startMakup(String, Hashtable)}
    * 
    * @param markupName
    *           Markup name to pasre
    * @param parameters
    *           PArameters to parse
    * @throws MissingRequiredParameterException
    *            If a parameter missing
    * @throws InvalidParameterValueException
    *            If a parameter is not valid
    */
   public void startMakup(String markupName, Hashtable<String, String> parameters) throws MissingRequiredParameterException, InvalidParameterValueException;

   /**
    * XML serialization
    * 
    * @return XML serialization
    */
   public MarkupXML toXML();

   /**
    * Valid a signature
    * 
    * @param message
    *           Message
    * @param signature
    *           Signature
    * @return {@code true} if signature valid
    * @throws IOException
    *            On read issue
    */
   public boolean validSignature(InputStream message, InputStream signature) throws IOException;

   /**
    * Serialize in XML stream
    * 
    * @param dynamicWriteXML
    *           Stream where write
    */
   public void writeInXML(DynamicWriteXML dynamicWriteXML);
}