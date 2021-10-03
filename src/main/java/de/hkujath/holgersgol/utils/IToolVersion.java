package de.hkujath.holgersgol.utils;

public interface IToolVersion {
  /** support mail address */
  String MAIL = "holger.kujath@gmx.de";

  /**
   * 
   * @return app name.
   *
   * @author Kujath, Holger
   * @since 01.10.2021
   */
  String getName();

  /**
   * 
   * @return build version.
   *
   * @author Kujath, Holger
   * @since 01.10.2021
   */
  String getBuild();

  /**
   * 
   * @return build date.
   *
   * @author Kujath, Holger
   * @since 01.10.2021
   */
  String getBuildDate();

  /**
   * 
   * @return ID.
   *
   * @author Kujath, Holger
   * @since 01.10.2021
   */
  String getId();
}
