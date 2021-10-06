package de.hkujath.holgersgol.data;

import de.hkujath.holgersgol.exceptions.GOLException;

public interface IGOLData extends Cloneable {

  /**
   * 
   * @return the current generation data.
   *
   * @author Kujath, Holger
   * @since 02.10.2021
   */
  int[][] getGridData();

  /**
   * Sets grid data.
   * 
   * @param inData the data to set to.
   * @throws GOLException if input is invalid.
   *
   * @author Kujath, Holger
   * @since 04.10.2021
   */
  void setGridData(int[][] inData) throws GOLException;

  /**
   * 
   * @return the grid size in x dimension.
   *
   * @author Kujath, Holger
   * @since 04.10.2021
   */
  int getGridSizeX();

  /**
   * 
   * @return the grid size in y dimension.
   *
   * @author Kujath, Holger
   * @since 04.10.2021
   */
  int getGridSizeY();

  /**
   * Initialize the data grid.
   * 
   * @throws GOLException if input data is invalid.
   *
   * @author Kujath, Holger
   * @since 03.10.2021
   */
  void initializeGrid() throws GOLException;

  /**
   * Initialize the data grid.
   * 
   * @param inData the data to set.
   * @throws GOLException if input data is invalid.
   *
   * @author Kujath, Holger
   * @since 04.10.2021
   */
  public void initializeGrid(int[][] inData) throws GOLException;

}
