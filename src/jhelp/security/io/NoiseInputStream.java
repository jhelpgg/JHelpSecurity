/**
 * Project : JHelpSecurity<br>
 * Package : jhelp.security.io<br>
 * Class : NoiseInputStream<br>
 * Date : 16 juil. 2010<br>
 * By JHelp
 */
package jhelp.security.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Stream to read "noise" stream create with {@link NoiseOutputStream} <br>
 * <br>
 * Last modification : 16 juil. 2010<br>
 * Version 0.0.0<br>
 * 
 * @author JHelp
 */
public class NoiseInputStream
      extends InputStream
{
   /** Stream to read */
   private final InputStream inputStream;
   /** Actual noise */
   private int               noise;

   /**
    * Constructs NoiseInputStream
    * 
    * @param inputStream
    *           Stream to read
    */
   public NoiseInputStream(final InputStream inputStream)
   {
      this.inputStream = inputStream;
      this.noise = 0;
   }

   /**
    * Close the stream
    * 
    * @throws IOException
    *            On closing issue
    * @see java.io.InputStream#close()
    */
   @Override
   public void close() throws IOException
   {
      this.inputStream.close();
   }

   /**
    * Read one byte
    * 
    * @return Byte read
    * @throws IOException
    *            On read issue
    * @see java.io.InputStream#read()
    */
   @Override
   public int read() throws IOException
   {
      while(this.noise == 0)
      {
         this.noise = this.inputStream.read();
      }

      this.noise--;

      return this.inputStream.read();
   }
}