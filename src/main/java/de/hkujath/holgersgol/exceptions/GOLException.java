package de.hkujath.holgersgol.exceptions;

public class GOLException extends Exception {

  /**
  	*
  	*/
  private static final long serialVersionUID = -6148649304069359021L;

  /**
   * 
   * @param inMsg error message.
   *
   * @author Kujath, Holger
   * @since 02.10.2021
   */
  public GOLException(String inMsg) {
    super(inMsg);
  }
}
