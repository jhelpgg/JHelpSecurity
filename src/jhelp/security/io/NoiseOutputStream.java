/**
 * Project : JHelpSecurity<br>
 * Package : jhelp.security.io<br>
 * Class : NoiseOutputStream<br>
 * Date : 16 juil. 2010<br>
 * By JHelp
 */
package jhelp.security.io;

import java.io.IOException;
import java.io.OutputStream;

import jhelp.util.list.Scramble;

/**
 * Stream write with "noise" (Additional bytes thats means nothing)<br>
 * <br>
 * Last modification : 16 juil. 2010<br>
 * Version 0.0.0<br>
 * 
 * @author JHelp
 */
public class NoiseOutputStream
      extends OutputStream
{
   /** Random choices */
   private static final int[] CHOICE;
   /** Random choice size */
   private static final int   SIZE_CHOICE;
   static
   {
      SIZE_CHOICE = 32896;
      final int[] choice = new int[NoiseOutputStream.SIZE_CHOICE];
      int index = 0;

      for(int bit = 0; bit < 256; bit++)
      {
         for(int time = 256 - bit; time > 0; time--)
         {
            choice[index++] = bit;
         }
      }

      Scramble.scramble(choice);

      CHOICE = choice;
   }

   /** Count noise maximum value */
   private int                count;

   /** Actual noise value */
   private int                noise;
   /** Stream where write */
   private final OutputStream outputStream;

   /**
    * Constructs NoiseOutputStream
    * 
    * @param outputStream
    *           Stream where write
    */
   public NoiseOutputStream(final OutputStream outputStream)
   {
      this.noise = 0;
      this.count = 3;
      this.outputStream = outputStream;
   }

   /**
    * Close the stream
    * 
    * @throws IOException
    *            On closing issue
    * @see java.io.OutputStream#close()
    */
   @Override
   public void close() throws IOException
   {
      this.outputStream.close();
   }

   /**
    * Flush last change
    * 
    * @throws IOException
    *            On flushing issue
    * @see java.io.OutputStream#flush()
    */
   @Override
   public void flush() throws IOException
   {
      this.outputStream.flush();
   }

   /**
    * Write on byte
    * 
    * @param b
    *           Byte to write
    * @throws IOException
    *            On writing issue
    * @see java.io.OutputStream#write(int)
    */
   @Override
   public void write(final int b) throws IOException
   {
      while(this.noise == 0)
      {
         this.noise = (int) (Math.random() * Math.min(this.count, NoiseOutputStream.CHOICE[(int) (Math.random() * NoiseOutputStream.SIZE_CHOICE)]));
         this.outputStream.write(this.noise);

         this.count++;
      }

      this.count++;
      if(this.count > 256)
      {
         this.count = 256;
      }

      this.noise--;
      this.outputStream.write(b);
   }
}