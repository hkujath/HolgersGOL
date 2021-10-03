package de.hkujath.holgersgol;

import de.hkujath.holgersgol.utils.IToolVersion;

public class Version implements IToolVersion {

  /** build version */
  private final static String BUILD     = "0.0.1-SNAPSHOT";
  /** build date */
  private final static String BUILDDATE = "2021/10/03";
  /** tool name */
  private final static String NAME      = "Holgers GOL";
  /** ID */
  private final static String ID        = NAME + " (" + BUILD + ")";

  /*
   * (non-Javadoc)
   * 
   * @see de.hkujath.holgersgol.utils.IToolVersion#getName()
   */
  @Override
  public String getName() {
    return NAME;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.hkujath.holgersgol.utils.IToolVersion#getBuild()
   */
  @Override
  public String getBuild() {
    return BUILD;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.hkujath.holgersgol.utils.IToolVersion#getBuildDate()
   */
  @Override
  public String getBuildDate() {
    return BUILDDATE;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.hkujath.holgersgol.utils.IToolVersion#getId()
   */
  @Override
  public String getId() {
    return ID;
  }

}
