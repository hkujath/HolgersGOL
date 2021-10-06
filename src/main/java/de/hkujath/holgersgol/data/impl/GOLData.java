package de.hkujath.holgersgol.data.impl;

import java.util.Random;
import de.hkujath.holgersgol.data.IGOLData;
import de.hkujath.holgersgol.exceptions.GOLException;
import de.hkujath.holgersgol.utils.GOLHelper;

public class GOLData implements IGOLData {

  /** grid data */
  private int[][] gridData;

  /** Grid X size */
  private int     gridSizeX;

  /** Grid Y size */
  private int     gridSizeY;

  /**
   * 
   * @param inGenData generation data
   * @param inGenNumber generation number
   * @throws GOLException if input data is invalid.
   *
   * @author Kujath, Holger
   * @since 02.10.2021
   */
  public GOLData(int maxSizeX, int maxSizeY) throws GOLException {
    if (maxSizeX < 1 || maxSizeY < 1) {
      throw new GOLException("Given max size is < 1.");
    }

    this.setGridData(new int[maxSizeY][maxSizeX]);
  }

  /**
   * Copy constructor
   * 
   * @param baseObject The base object to copy from.
   * @throws GOLException if input data is invalid.
   *
   * @author Kujath, Holger
   * @since 04.10.2021
   */
  public GOLData(IGOLData baseObject) throws GOLException {
    this.setGridData(baseObject.getGridData());
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.hkujath.holgersgol.data.IGOLData#getGridData()
   */
  @Override
  public int[][] getGridData() {
    return this.gridData;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.hkujath.holgersgol.data.IGOLData#setGridData(int[][])
   */
  @Override
  public void setGridData(int[][] inData) throws GOLException {
    if (inData == null) {
      throw new GOLException("Given input data is null.");
    }

    this.gridData = GOLHelper.clone2DArray(inData);
    this.gridSizeX = inData[0].length;
    this.gridSizeY = inData.length;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.hkujath.holgersgol.data.IGOLData#getGridSizeX()
   */
  @Override
  public int getGridSizeX() {
    return this.gridSizeX;

  }

  /*
   * (non-Javadoc)
   * 
   * @see de.hkujath.holgersgol.data.IGOLData#getGridSizeY()
   */
  @Override
  public int getGridSizeY() {
    return this.gridSizeY;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.hkujath.holgersgol.data.IGOLData#initializeGrid()
   */
  @Override
  public void initializeGrid() throws GOLException {

    if (this.gridData == null) {
      throw new GOLException("Grid data is null.");
    }

    Random r = new Random();
    for (int[] row : this.gridData) {
      for (int x = 0; x < row.length; x++) {
        row[x] = r.nextInt(2);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.hkujath.holgersgol.data.IGOLData#initializeGrid(int[][])
   */
  @Override
  public void initializeGrid(int[][] inData) throws GOLException {
    setGridData(inData);
  }

}
