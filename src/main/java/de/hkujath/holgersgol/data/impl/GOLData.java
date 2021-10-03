package de.hkujath.holgersgol.data.impl;

import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hkujath.holgersgol.MainController;
import de.hkujath.holgersgol.data.IGOLData;
import de.hkujath.holgersgol.exceptions.GOLException;

public class GOLData implements IGOLData {

  /** Generation data */
  private int[][]             generationData;

  /** The current generation */
  private int                 currentGeneration;

  /** Y size */
  private int                 sizeX;

  /** X size */
  private int                 sizeY;

  /** logger instance */
  private static final Logger LOG = LoggerFactory.getLogger(GOLData.class);

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

    this.generationData = new int[maxSizeY][maxSizeX];
    this.currentGeneration = 0;
    this.sizeX = maxSizeX;
    this.sizeY = maxSizeY;
  }

  /**
   * 
   * @return the current generation data.
   *
   * @author Kujath, Holger
   * @since 02.10.2021
   */
  public int[][] getGenerationData() {
    return this.generationData.clone();
  }

  /**
   * 
   * @return the current generation number.
   *
   * @author Kujath, Holger
   * @since 03.10.2021
   */
  public int getNumCurrentGeneration() {
    return this.currentGeneration;
  }

  /**
   * @return Liefert den Wert von sizeX.
   */

  public int getSizeX() {
    return this.sizeX;
  }

  /**
   * @return Liefert den Wert von sizeY.
   */

  public int getSizeY() {
    return this.sizeY;
  }

  /**
   * 
   * 
   *
   * @author Kujath, Holger
   * @since 02.10.2021
   */
  public void calcNextGeneration() throws GOLException {

    int[][] newGenData = GOLData.clone2DArray(this.generationData);

    int dataMaxSizeX = this.getSizeX();
    int dataMaxSizeY = this.getSizeY();

    for (int yPos = 0; yPos < dataMaxSizeY; yPos++) {

      for (int xPos = 0; xPos < dataMaxSizeX; xPos++) {
        int numLiveNeighbours = GOLData.calcNumLiveNeighbours(this.generationData, xPos, yPos);
        int curCellState = this.generationData[yPos][xPos];

        if (curCellState == 1) {
          //Check rule 1
          // "Any live cell with fewer than two live neighbours dies, as if caused by underpopulation."
          if (numLiveNeighbours < 2) {
            newGenData[yPos][xPos] = 0;
            continue;
          }
          //Check rule 2
          // "Any live cell with more than three live neighbours dies, as if by overcrowding."
          if (numLiveNeighbours > 3) {
            newGenData[yPos][xPos] = 0;
            continue;
          }
          //Check rule 3
          // "Any live cell with two or three live neighbours lives on to the next generation."
          newGenData[yPos][xPos] = 1;
        } else {
          //Check rule 4
          // "Any dead cell with exactly three live neighbours becomes a live cell."
          if (numLiveNeighbours == 3) {
            newGenData[yPos][xPos] = 1;
          }
        }

      }
    }
    this.generationData = newGenData;
    this.currentGeneration += 1;
  }

  /**
   * 
   * @param inGridData
   * @param inXPos position in x dimension (starts with 0)
   * @param inYPos position in y dimension (starts with 0)
   * @return
   * @throws GOLException
   *
   * @author Kujath, Holger
   * @since 02.10.2021
   */
  public static int calcNumLiveNeighbours(int[][] inGridData, int inXPos, int inYPos) throws GOLException {

    if (inGridData == null || inGridData.length == 0) {
      throw new GOLException("Given grid data is invalid.");
    }

    int dataMaxSizeX = inGridData[0].length;
    int dataMaxSizeY = inGridData.length;

    if (inXPos < 0 || inXPos >= dataMaxSizeX) {
      throw new GOLException("X position is outside bounds.");
    }

    if (inYPos < 0 || inYPos >= dataMaxSizeY) {
      throw new GOLException("Y position is outside bounds.");
    }

    //printGrid(inGridData);

    int numLiveNeighbours = 0;

    for (int x : new int[] { -1, 0, 1 }) {

      for (int y : new int[] { -1, 0, 1 }) {
        int checkXPos = inXPos + x;
        int checkYPos = inYPos + y;

        if (checkXPos < 0 || checkXPos >= dataMaxSizeX || checkYPos < 0 || checkYPos >= dataMaxSizeY
            || (checkXPos == inXPos && checkYPos == inYPos)) {
          continue;
        }

        numLiveNeighbours = inGridData[checkYPos][checkXPos] == 1 ? numLiveNeighbours + 1 : numLiveNeighbours;
      }
    }

    return numLiveNeighbours;

  }

  /**
   * 
   * @param inData the array to copy frm.
   * @return a deep copy of the given 2D array.
   * @throws GOLException
   *
   * @author Kujath, Holger
   * @since 03.10.2021
   */
  public static int[][] clone2DArray(int[][] inData) throws GOLException {

    if (inData == null) {
      throw new GOLException("Given input data to clone is invalid.");
    }

    return Arrays.stream(inData).map(int[]::clone).toArray(int[][]::new);
  }

  /**
   * This function prints the grid data.
   * 
   * @param inGridData The grid data.
   * @throws GOLException if input is invalid.
   *
   * @author Kujath, Holger
   * @since 03.10.2021
   */
  public static void printGrid(int[][] inGridData) throws GOLException {
    if (inGridData == null || inGridData.length == 0) {
      throw new GOLException("Given grid data is invalid.");
    }

    //LOG.debug("####Printing grid data ###");

    Arrays.stream(inGridData).forEach(x -> {
      String result = Arrays.stream(x).mapToObj(String::valueOf).collect(Collectors.joining(" "));
      LOG.debug(result);
    });
    //LOG.debug("### Finished ###");
  }

  /**
   * Initialise the data grid.
   * 
   * @throws GOLException
   *
   * @author Kujath, Holger
   * @since 03.10.2021
   */
  public void initializeGrid() throws GOLException {

    if (this.generationData == null) {
      throw new GOLException("Given input data is null.");
    }

    Random r = new Random();
    for (int[] row : this.generationData) {
      for (int x = 0; x < row.length; x++) {
        row[x] = r.nextInt(2);
      }
    }
  }
}
