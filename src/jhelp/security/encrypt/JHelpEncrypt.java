/**
 * Project : JHelpSecurity<br>
 * Package : jhelp.security.encrypt<br>
 * Class : JHelpEncrypt<br>
 * Date : 8 juil. 2010<br>
 * By JHelp
 */
package jhelp.security.encrypt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Encrypt/decrypt something with a secret key <br>
 * <br>
 * Last modification : 8 juil. 2010<br>
 * Version 0.0.0<br>
 * 
 * @author JHelp
 */
public interface JHelpEncrypt
{
   /**
    * Encrypt a stream
    * 
    * @param key
    *           Key for encrypt
    * @param clearStream
    *           Stream to encrypt
    * @param encryptStream
    *           Encrypted stream
    * @throws IOException
    *            On reading/writing problem
    */
   public void encrypt(byte[] key, InputStream clearStream, OutputStream encryptStream) throws IOException;;

   /**
    * Decrypt a stream
    * 
    * @param key
    *           Key for decrypt
    * @param encryptStream
    *           Stream to decrypt
    * @param clearStream
    *           Decrypted stream
    * @throws IOException
    *            On reading/writing problem
    */
   public void decrypt(byte[] key, InputStream encryptStream, OutputStream clearStream) throws IOException;;
}