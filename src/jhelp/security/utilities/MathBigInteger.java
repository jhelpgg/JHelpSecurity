/**
 * Project : JHelpSecurity<br>
 * Package : jhelp.security.utilities<br>
 * Class : MathBigInteger<br>
 * Date : 8 juil. 2010<br>
 * By JHelp
 */
package jhelp.security.utilities;

import java.math.BigInteger;
import java.util.Random;

/**
 * <br>
 * <br>
 * Last modification : 8 juil. 2010<br>
 * Version 0.0.0<br>
 * 
 * @author JHelp
 */
public class MathBigInteger
{
   /**
    * Zero constant
    */
   public static final BigInteger ZERO           = new BigInteger("0");
   /**
    * One constant
    */
   public static final BigInteger ONE            = new BigInteger("1");
   /**
    * Two constant
    */
   public static final BigInteger TWO            = new BigInteger("2");
   /**
    * The minimum order
    */
   public static final BigInteger ORDER_MINMUM   = new BigInteger("256");
   /**
    * Maximum of the order : 2<sup>ORDER_NUM_BITS</sup>
    */
   public static final int        ORDER_NUM_BITS = 63;
   /**
    * Random function
    */
   private final static Random    RANDOM         = new Random();

   /**
    * Indicates if an big integer is prime or not
    * 
    * @param bigInteger
    *           The big integer to test
    * @return {@code true} if the big integer is prime
    */
   public static boolean isPrime(BigInteger bigInteger)
   {
      return bigInteger.isProbablePrime(Integer.MAX_VALUE);
   }

   /**
    * Get a random prime number between 0 to 2<sup>numBits</sup>
    * 
    * @param numBits
    *           The way to precise the maximum
    * @return The prime number
    */
   public static BigInteger getPrimeNumber(int numBits)
   {
      BigInteger bigInteger = new BigInteger(numBits, MathBigInteger.RANDOM);
      while(!MathBigInteger.isPrime(bigInteger))
      {
         bigInteger = new BigInteger(numBits, MathBigInteger.RANDOM);
      }
      return bigInteger;
   }

   /**
    * Create a big integer from a long
    * 
    * @param integer
    *           the big integer to create
    * @return The big integer created
    */
   public static BigInteger createBigInteger(long integer)
   {
      return new BigInteger(String.valueOf(integer));
   }

   /**
    * Get a random big integer between two others
    * 
    * @param minimum
    *           The minimum value
    * @param maximum
    *           The maximum value
    * @return The chosen value
    */
   public static BigInteger getRandomBigInteger(BigInteger minimum, BigInteger maximum)
   {
      int numBits = Math.max(minimum.bitLength(), maximum.bitLength());
      BigInteger integer = new BigInteger(numBits, MathBigInteger.RANDOM);
      while(integer.compareTo(minimum) <= 0 || integer.compareTo(maximum) >= 0)
      {
         integer = new BigInteger(numBits, MathBigInteger.RANDOM);
      }
      return integer;
   }

   /**
    * Get a random big integer between two and an other one
    * 
    * @param maximum
    *           The maximum value
    * @return The chosen value
    */
   public static BigInteger getRandomBigInteger(BigInteger maximum)
   {
      return MathBigInteger.getRandomBigInteger(MathBigInteger.TWO, maximum);
   }

   /**
    * Get a random prime number less a value
    * 
    * @param maximum
    *           The maximum value
    * @return The chosen prime number
    */
   public static BigInteger getRandomPrimeBigInteger(BigInteger maximum)
   {
      BigInteger prime = MathBigInteger.getRandomBigInteger(maximum);
      while(!MathBigInteger.isPrime(prime))
      {
         prime = MathBigInteger.getRandomBigInteger(maximum);
      }
      return prime;
   }

   /**
    * Get a valid order for the Elgamal algorithm
    * 
    * @return An order
    */
   public static BigInteger getOrder()
   {
      BigInteger order = MathBigInteger.getPrimeNumber(MathBigInteger.ORDER_NUM_BITS);
      while(order.compareTo(MathBigInteger.ORDER_MINMUM) < 0)
      {
         order = MathBigInteger.getPrimeNumber(MathBigInteger.ORDER_NUM_BITS);
      }
      return order;
   }
}