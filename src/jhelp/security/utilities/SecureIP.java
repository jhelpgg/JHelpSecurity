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
    * Decrypt an IP
    * 
    * @param encoded
    *           Zncoded IP
    * @return Clear IP
    * @throws IOException
    *            On decryption issue
    */
   public static InetAddress decryptIP(final String encoded) throws IOException
   {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(JHelpBase.decode(encoded));
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

      DESencrypt.DES.decrypt("JHelp", byteArrayInputStream, byteArrayOutputStream);

      final byte[] message = byteArrayOutputStream.toByteArray();
      final int length = message.length - 16;
      final byte[] crypted = new byte[length];
      final byte[] byteRnd = new byte[8];
      final byte[] byteTime = new byte[8];

      System.arraycopy(message, 0, byteRnd, 0, 4);
      System.arraycopy(message, 4, byteTime, 0, 4);
      System.arraycopy(message, 8, crypted, 0, length);
      System.arraycopy(message, 8 + length, byteTime, 4, 4);
      System.arraycopy(message, 12 + length, byteRnd, 4, 4);

      final double rnd = UtilIO.byteArrayToDouble(byteRnd);
      final long time = UtilIO.byteArrayToLong(byteTime);

      byteArrayInputStream = new ByteArrayInputStream(crypted);
      byteArrayOutputStream = new ByteArrayOutputStream();

      UtilEncrpyt.tripleDESdecrypt(String.valueOf(time), "JHelp", String.valueOf(rnd), byteArrayInputStream, byteArrayOutputStream);

      return InetAddress.getByAddress(byteArrayOutputStream.toByteArray());
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
   public static String encryptIP(final InetAddress address) throws IOException
   {
      final byte[] ip = address.getAddress();
      final double rnd = Math.random();
      final long time = System.currentTimeMillis();

      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ip);
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

      UtilEncrpyt.tripleDESencrypt(String.valueOf(time), "JHelp", String.valueOf(rnd), byteArrayInputStream, byteArrayOutputStream);
      final byte[] crypted = byteArrayOutputStream.toByteArray();

      final int length = crypted.length;
      final byte[] message = new byte[length + 16];

      final byte[] byteRnd = UtilIO.doubleToByteArray(rnd);
      final byte[] byteTime = UtilIO.longToByteArray(time);

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
}