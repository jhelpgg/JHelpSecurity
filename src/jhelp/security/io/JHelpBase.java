/**
 * Project : JHelpSecurity<br>
 * Package : jhelp.security.io<br>
 * Class : JHelpBase<br>
 * Date : 17 juil. 2010<br>
 * By JHelp
 */
package jhelp.security.io;

/**
 * Base 64 not official<br>
 * <br>
 * Last modification : 17 juil. 2010<br>
 * Version 0.0.0<br>
 * 
 * @author JHelp
 */
public class JHelpBase
{
   /** Symbols reference */
   private static final char[] SYMBOLS =
                                       {
         '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'z', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'q', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'w', 'x', 'c', 'v', 'b', 'n', 'A', 'Z', 'E', 'R', 'T', 'Y', 'U', 'I', 'O',
         'P', 'Q', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'W', 'X', 'C', 'V', 'B', 'N', '+', '-'
                                       };

   /**
    * Index of a character
    * 
    * @param c
    *           Character
    * @return Character index
    */
   private static final int indexOf(char c)
   {
      for(int index = 0; index < 64; index++)
      {
         if(JHelpBase.SYMBOLS[index] == c)
         {
            return index;
         }
      }

      return -1;
   }

   /**
    * Encode an integer
    * 
    * @param integer
    *           Integer to encode
    * @return Encoded integer
    */
   private static String encodeInt(int integer)
   {
      return new String(new char[]
      {
            JHelpBase.SYMBOLS[integer >> 18 & 0x3F], JHelpBase.SYMBOLS[integer >> 12 & 0x3F], JHelpBase.SYMBOLS[integer >> 6 & 0x3F], JHelpBase.SYMBOLS[integer & 0x3F]
      });
   }

   /**
    * Decode an integer
    * 
    * @param car
    *           Characters to decode
    * @return Decoded integer
    */
   private static int decodeInt(char[] car)
   {
      return JHelpBase.indexOf(car[0]) << 18 | JHelpBase.indexOf(car[1]) << 12 | JHelpBase.indexOf(car[2]) << 6 | JHelpBase.indexOf(car[3]);
   }

   /**
    * Encode a byte array (size must be &lt; 2<sup>18</sup>)
    * 
    * @param data
    *           Array to encode
    * @return Encoded array
    */
   public static String encode(byte[] data)
   {
      int length = data.length;

      StringBuilder stringBuilder = new StringBuilder((length * 4) / 3 + 1);

      stringBuilder.append(JHelpBase.encodeInt(length));

      int first = 0;
      int second = 0;
      int third = 0;
      int index = 0;

      while(index < length)
      {
         second = third = 0;

         first = data[index++] & 0xFF;

         if(index < length)
         {
            second = data[index++] & 0xFF;

            if(index < length)
            {
               third = data[index++] & 0xFF;
            }
         }

         stringBuilder.append(JHelpBase.SYMBOLS[first >> 2 & 0x3f]);
         stringBuilder.append(JHelpBase.SYMBOLS[((first & 0x3) << 4) | ((second >> 4) & 0xF)]);
         stringBuilder.append(JHelpBase.SYMBOLS[((second & 0xF) << 2) | ((third >> 6) & 0x3)]);
         stringBuilder.append(JHelpBase.SYMBOLS[third & 0x3f]);
      }

      return stringBuilder.toString();
   }

   /**
    * Decode an array of byte
    * 
    * @param coded
    *           Coded array
    * @return Clear array
    */
   public static byte[] decode(String coded)
   {
      char[] car = coded.toCharArray();

      int length = JHelpBase.decodeInt(car);
      int indexRead = 4;
      int indexWrite = 0;
      byte[] data = new byte[length];

      int first = 0;
      int second = 0;
      int third = 0;
      int fourth = 0;

      while(indexWrite < length)
      {
         first = JHelpBase.indexOf(car[indexRead++]);
         second = JHelpBase.indexOf(car[indexRead++]);
         third = JHelpBase.indexOf(car[indexRead++]);
         fourth = JHelpBase.indexOf(car[indexRead++]);

         data[indexWrite++] = (byte) ((first << 2) | ((second >> 4) & 0x3));
         if(indexWrite < length)
         {
            data[indexWrite++] = (byte) (((second & 0xF) << 4) | ((third >> 2) & 0xF));
            if(indexWrite < length)
            {
               data[indexWrite++] = (byte) (((third & 0x3) << 6) | fourth);
            }
         }
      }

      return data;
   }
}