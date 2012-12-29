/**
 * Project : JHelpSecurity<br>
 * Package : jhelp.security.utilities<br>
 * Class : UtilEncrpyt<br>
 * Date : 25 juil. 2010<br>
 * By JHelp
 */
package jhelp.security.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import jhelp.security.encrypt.desEncrypt.DESencrypt;
import jhelp.security.io.NoiseInputStream;
import jhelp.security.io.NoiseOutputStream;
import jhelp.security.pulicPrivateKey.FactoryKeyPairs;
import jhelp.security.pulicPrivateKey.JHelpKeyPairs;
import jhelp.security.pulicPrivateKey.JHelpPublicKey;
import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.io.UtilIO;
import jhelp.util.text.UtilText;

/**
 * Encrypt utilities <br>
 * <br>
 * Last modification : 25 juil. 2010<br>
 * Version 0.0.0<br>
 * 
 * @author JHelp
 */
public class UtilEncrpyt
{
   /** Decryption way */
   public static final String MODE_DECRYPT = "decrypt";
   /** Encryption way */
   public static final String MODE_ENCRYPT = "encrypt";

   /**
    * Print the usage
    */
   private static void printUsage()
   {
      Debug.println(DebugLevel.INFORMATION, "UtilEncrpyt <mode> <password> <pathSource> <pathDestination>");
      Debug.println(DebugLevel.INFORMATION, "\t<mode>\t\t\t:\tWay of encryption it is 'encrypt' or 'decrypt'");
      Debug.println(DebugLevel.INFORMATION, "\t<password>\t\t:\tPassword to use");
      Debug.println(DebugLevel.INFORMATION, "\t<pathSource>\t\t:\tSource file to be encrypt or decrypt");
      Debug.println(DebugLevel.INFORMATION, "\t<pathDestination>\t:\tDestination file where write encrypted/decrypted data");
   }

   /**
    * Decrypt a file encrypt with {@link #encrypt(String, File, File)}. Think about give the good name to the encrypted file,
    * else decryption will failed
    * 
    * @param password
    *           Password
    * @param source
    *           Encrypted file
    * @param destination
    *           Clear file to write
    */
   public static void decrypt(final String password, final File source, final File destination)
   {
      try
      {
         final File privateKey = UtilIO.obtainExternalFile("tempUtilEncrpyt");
         UtilIO.createFile(privateKey);

         final String name = source.getName();
         final String other = UtilText.concatenate(password, "_JHELP_", name);

         final ZipInputStream zipInputStream = new ZipInputStream(new NoiseInputStream(new NoiseInputStream(new NoiseInputStream(new FileInputStream(source)))));
         ZipEntry zipEntry = zipInputStream.getNextEntry();
         if(zipEntry.getName().equals("a") == false)
         {
            throw new Exception("First is " + zipEntry.getName());
         }

         FileOutputStream fileOutputStream = new FileOutputStream(privateKey);
         UtilEncrpyt.tripleDESdecrypt(password, name, other, zipInputStream, fileOutputStream);
         zipInputStream.closeEntry();
         fileOutputStream.close();

         final JHelpKeyPairs<JHelpPublicKey> keyPair = FactoryKeyPairs.obtainKeyPair(FactoryKeyPairs.ALGORITHM_RSA);
         final byte[] key = DESencrypt.computeKey(password);
         keyPair.loadPair("tempUtilEncrpyt", key);
         UtilIO.delete(privateKey);

         UtilIO.createFile(destination);
         zipEntry = zipInputStream.getNextEntry();
         if(zipEntry.getName().equals("b") == false)
         {
            throw new Exception("Second is " + zipEntry.getName());
         }

         fileOutputStream = new FileOutputStream(destination);
         keyPair.decrypt(zipInputStream, fileOutputStream);
         zipInputStream.closeEntry();
         fileOutputStream.close();

         zipInputStream.close();
      }
      catch(final Exception exception)
      {
         Debug.printException(exception);
         throw new RuntimeException();
      }
   }

   /**
    * Encrypt a file. Can be decrypt with {@link #decrypt(String, File, File)}. Beware don't rename the destination file, or
    * remember its orignal name. The file name is use inside the encryption, it can add some security if you rename the file
    * then rename it to original name before decrypt it
    * 
    * @param password
    *           Password
    * @param source
    *           File to encrypt
    * @param destination
    *           Encrypted file
    */
   public static void encrypt(final String password, final File source, final File destination)
   {
      try
      {
         UtilIO.createFile(destination);
         final String name = destination.getName();
         final String other = UtilText.concatenate(password, "_JHELP_", name);

         final byte[] key = DESencrypt.computeKey(password);

         final JHelpKeyPairs<JHelpPublicKey> keyPair = FactoryKeyPairs.obtainKeyPair(FactoryKeyPairs.ALGORITHM_RSA);
         keyPair.storePair("tempUtilEncrpyt", key);

         final File privateKey = UtilIO.obtainExternalFile("tempUtilEncrpyt");

         FileInputStream fileInputStream = new FileInputStream(privateKey);
         final ZipOutputStream zipOutputStream = new ZipOutputStream(new NoiseOutputStream(new NoiseOutputStream(new NoiseOutputStream(new FileOutputStream(destination)))));

         ZipEntry zipEntry = new ZipEntry("a");
         zipOutputStream.putNextEntry(zipEntry);

         UtilEncrpyt.tripleDESencrypt(password, name, other, fileInputStream, zipOutputStream);

         fileInputStream.close();
         zipOutputStream.flush();
         zipOutputStream.closeEntry();
         UtilIO.delete(privateKey);

         fileInputStream = new FileInputStream(source);
         zipEntry = new ZipEntry("b");
         zipOutputStream.putNextEntry(zipEntry);
         keyPair.getPublicKey().encrypt(fileInputStream, zipOutputStream);
         fileInputStream.close();
         zipOutputStream.flush();
         zipOutputStream.closeEntry();

         zipOutputStream.flush();
         zipOutputStream.close();
      }
      catch(final Exception exception)
      {
         Debug.printException(exception);
         throw new RuntimeException();
      }
   }

   /**
    * For be use in batch mode. Usage : <code>
    * UtilEncrpyt &lt;mode&gt; &lt;password&gt; &lt;pathSource&gt; &lt;pathDestination&gt;<br>
    * &lt;mode&gt;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;Way of encryption it is 'encrypt' or 'decrypt'<br>
    * &lt;password&gt;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;Password to use<br>
    * &lt;pathSource&gt;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;Source file to encrypt/decrypt<br>
    * &lt;pathDestination&gt;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;Encrypted/clear file
    * </code>
    * 
    * @param args
    *           Arguments
    */
   public static void main(final String[] args)
   {
      if(args == null || args.length < 4)
      {
         UtilEncrpyt.printUsage();

         return;
      }

      try
      {
         boolean encrypt = true;
         final String mode = args[0].toLowerCase();

         if(UtilEncrpyt.MODE_ENCRYPT.equals(mode) == true)
         {
            encrypt = true;
         }
         else if(UtilEncrpyt.MODE_DECRYPT.equals(mode) == true)
         {
            encrypt = false;
         }
         else
         {
            UtilEncrpyt.printUsage();

            return;
         }

         final String password = args[1];
         final File source = new File(args[2]);
         final File destination = new File(args[3]);

         if(encrypt == true)
         {
            UtilEncrpyt.encrypt(password, source, destination);
         }
         else
         {
            UtilEncrpyt.decrypt(password, source, destination);
         }
      }
      catch(final Exception exception)
      {
         Debug.println(DebugLevel.INFORMATION, exception);

         Debug.println(DebugLevel.INFORMATION);
         Debug.println(DebugLevel.INFORMATION, "--------------------------------");
         Debug.println(DebugLevel.INFORMATION);

         UtilEncrpyt.printUsage();
      }
   }

   /**
    * Decrypt an encrypted stream by {@link #tripleDESencrypt(String, String, String, InputStream, OutputStream)}
    * 
    * @param key1
    *           First key
    * @param key2
    *           Second key
    * @param key3
    *           Third key
    * @param inputStream
    *           Encrypted stream
    * @param outputStream
    *           Clear stream where write
    * @throws IOException
    *            On read/write issue
    */
   public static void tripleDESdecrypt(final String key1, final String key2, final String key3, final InputStream inputStream, final OutputStream outputStream) throws IOException
   {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      DESencrypt.DES.decrypt(key1, inputStream, byteArrayOutputStream);

      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
      byteArrayOutputStream = new ByteArrayOutputStream();
      DESencrypt.DES.decrypt(key2, byteArrayInputStream, byteArrayOutputStream);

      byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
      DESencrypt.DES.decrypt(key3, byteArrayInputStream, outputStream);
   }

   /**
    * Encrypt in Triple DES. Can be decrypt with {@link #tripleDESdecrypt(String, String, String, InputStream, OutputStream)}
    * 
    * @param key1
    *           First key
    * @param key2
    *           Second key
    * @param key3
    *           Third key
    * @param inputStream
    *           Stream to encrypt
    * @param outputStream
    *           Encrypted stream
    * @throws IOException
    *            On read/write issue
    */
   public static void tripleDESencrypt(final String key1, final String key2, final String key3, final InputStream inputStream, final OutputStream outputStream) throws IOException
   {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      DESencrypt.DES.encrypt(key3, inputStream, byteArrayOutputStream);

      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
      byteArrayOutputStream = new ByteArrayOutputStream();
      DESencrypt.DES.encrypt(key2, byteArrayInputStream, byteArrayOutputStream);

      byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
      DESencrypt.DES.encrypt(key1, byteArrayInputStream, outputStream);
   }
}