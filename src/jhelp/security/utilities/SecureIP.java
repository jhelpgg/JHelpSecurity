/**
 * Project : JHelpSecurity<br>
 * Package : jhelp.security.utilities<br>
 * Class : SecureIP<br>
 * Date : 9 nov. 2010<br>
 * By JHelp
 */
package jhelp.security.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;

import jhelp.security.encrypt.desEncrypt.DESencrypt;
import jhelp.security.io.JHelpBase;
import jhelp.util.io.UtilIO;

/**
 * For store IP in secure mode<br>
 * <br>
 * Last modification : 9 nov. 2010<br>
 * Version 0.0.0<br>
 * 
 * @author JHelp
 */
public class SecureIP
{
   /**
    * Encrypt local IP
    * 
    * @return Encrypted local IP
    * @throws IOException
    *            On encryption issue
    */
   public static String encryptLocalIP() throws IOException
   {
      return SecureIP.encryptIP(InetAddress.getLocalHost());
   }

   /**
    * Encrypt an IP
    * 
    * @param address
    *           Address to encrypt
    * @return Encrypted IP
    * @throws IOException
    *            On encryption issue
    */
   public static String encryptIP(InetAddress address) throws IOException
   {
      byte[] ip = address.getAddress();
      double rnd = Math.random();
      long time = System.currentTimeMillis();

      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ip);
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

      UtilEncrpyt.tripleDESencrypt(String.valueOf(time), "JHelp", String.valueOf(rnd), byteArrayInputStream, byteArrayOutputStream);
      byte[] crypted = byteArrayOutputStream.toByteArray();

      int length = crypted.length;
      byte[] message = new byte[length + 16];

      byte[] byteRnd = UtilIO.doubleToByteArray(rnd);
      byte[] byteTime = UtilIO.longToByteArray(time);

      System.arraycopy(byteRnd, 0, message, 0, 4);
      System.arraycopy(byteTime, 0, message, 4, 4);
      System.arraycopy(crypted, 0, message, 8, length);
      System.arraycopy(byteTime, 4, message, 8 + length, 4);
      System.arraycopy(byteRnd, 4, message, 12 + length, 4);

      byteArrayInputStream = new ByteArrayInputStream(message);
      byteArrayOutputStream = new ByteArrayOutputStream();

      DESencrypt.DES.encrypt("JHelp", byteArrayInputStream, byteArrayOutputStream);

      return JHelpBase.encode(byteArrayOutputStream.toByteArray());
   }

   /**
    * Decrypt an IP
    * 
    * @param encoded
    *           Zncoded IP
    * @return Clear IP
    * @throws IOException
    *            On decryption issue
    */
   public static InetAddress decryptIP(String encoded) throws IOException
   {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(JHelpBase.decode(encoded));
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

      DESencrypt.DES.decrypt("JHelp", byteArrayInputStream, byteArrayOutputStream);

      byte[] message = byteArrayOutputStream.toByteArray();
      int length = message.length - 16;
      byte[] crypted = new byte[length];
      byte[] byteRnd = new byte[8];
      byte[] byteTime = new byte[8];

      System.arraycopy(message, 0, byteRnd, 0, 4);
      System.arraycopy(message, 4, byteTime, 0, 4);
      System.arraycopy(message, 8, crypted, 0, length);
      System.arraycopy(message, 8 + length, byteTime, 4, 4);
      System.arraycopy(message, 12 + length, byteRnd, 4, 4);

      double rnd = UtilIO.byteArrayToDouble(byteRnd);
      long time = UtilIO.byteArrayToLong(byteTime);

      byteArrayInputStream = new ByteArrayInputStream(crypted);
      byteArrayOutputStream = new ByteArrayOutputStream();

      UtilEncrpyt.tripleDESdecrypt(String.valueOf(time), "JHelp", String.valueOf(rnd), byteArrayInputStream, byteArrayOutputStream);

      return InetAddress.getByAddress(byteArrayOutputStream.toByteArray());
   }
}