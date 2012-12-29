/**
 * Project : JHelpSecurity<br>
 * Package : jhelp.security.pulicPrivateKey<br>
 * Class : NoSuchAlgorithmException<br>
 * Date : 16 juil. 2010<br>
 * By JHelp
 */
package jhelp.security.pulicPrivateKey;

import jhelp.util.text.UtilText;

/**
 * Throw if an algorithm is unknown<br>
 * <br>
 * Last modification : 16 juil. 2010<br>
 * Version 0.0.0<br>
 * 
 * @author JHelp
 */
public class NoSuchAlgorithmException
      extends RuntimeException
{
   /** serialVersionUID */
   private static final long serialVersionUID = -5787604001507253790L;

   /**
    * Constructs NoSuchAlgorithmException
    * 
    * @param algorithm
    *           Algorithm name
    * @param cause
    *           Cause
    */
   public NoSuchAlgorithmException(final String algorithm, final Throwable cause)
   {
      super(UtilText.concatenate("Algorithm ", algorithm, " doesn't exits !"), cause);
   }
}