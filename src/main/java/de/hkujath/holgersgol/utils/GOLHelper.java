package de.hkujath.holgersgol.utils;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.hkujath.holgersgol.exceptions.GOLException;

public class GOLHelper {

  /** logger instance */
  private static final Logger LOG = LoggerFactory.getLogger(GOLHelper.class);

  /**
   * Constructor
   * 
   *
   * @author Kujath, Holger
   * @since 04.10.2021
   */
  private GOLHelper() {
    // Made private
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
      throw new GOLException("Given array is null.");
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
  public static void print2DArray(int[][] inData) throws GOLException {

    if (inData == null) {
      throw new GOLException("Given data array is null.");
    }
    // LOG.debug("####Printing grid data ###");

    Arrays.stream(inData).forEach(x -> {
      String result = Arrays.stream(x).mapToObj(String::valueOf).collect(Collectors.joining(" "));
      LOG.debug(result);
    });
    // LOG.debug("### Finished ###");
  }

  /**
   * 
   * 
   *
   * @author Kujath, Holger
   * @since 02.10.2021
   */
  public static int[][] calcNextGeneration(int[][] inData) throws GOLException {

    if (inData == null) {
      throw new GOLException("Given data array is null.");
    }

    int[][] newGenData = GOLHelper.clone2DArray(inData);

    int dataMaxSizeX = inData[0].length;
    int dataMaxSizeY = inData.length;

    for (int yPos = 0; yPos < dataMaxSizeY; yPos++) {

      for (int xPos = 0; xPos < dataMaxSizeX; xPos++) {
        int numLiveNeighbours = GOLHelper.calcNumLiveNeighbours(inData, xPos, yPos);
        int curCellState = inData[yPos][xPos];

        if (curCellState == 1) {
          // Check rule 1
          // "Any live cell with fewer than two live neighbours dies, as if caused by
          // underpopulation."
          if (numLiveNeighbours < 2) {
            newGenData[yPos][xPos] = 0;
            continue;
          }
          // Check rule 2
          // "Any live cell with more than three live neighbours dies, as if by
          // overcrowding."
          if (numLiveNeighbours > 3) {
            newGenData[yPos][xPos] = 0;
            continue;
          }
          // Check rule 3
          // "Any live cell with two or three live neighbours lives on to the next
          // generation."
          newGenData[yPos][xPos] = 1;
        } else {
          // Check rule 4
          // "Any dead cell with exactly three live neighbours becomes a live cell."
          if (numLiveNeighbours == 3) {
            newGenData[yPos][xPos] = 1;
          }
        }

      }
    }
    return newGenData;
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
  public static int calcNumLiveNeighbours(int[][] inData, int inXPos, int inYPos) throws GOLException {

    if (inData == null) {
      throw new GOLException("Given data array is null.");
    }

    int dataMaxSizeX = inData[0].length;
    int dataMaxSizeY = inData.length;

    if (inXPos < 0 || inXPos >= dataMaxSizeX) {
      throw new GOLException("X position is outside bounds.");
    }

    if (inYPos < 0 || inYPos >= dataMaxSizeY) {
      throw new GOLException("Y position is outside bounds.");
    }

    int numLiveNeighbours = 0;

    for (int x : new int[] { -1, 0, 1 }) {

      for (int y : new int[] { -1, 0, 1 }) {
        int checkXPos = inXPos + x;
        int checkYPos = inYPos + y;

        if (checkXPos < 0 || checkXPos >= dataMaxSizeX || checkYPos < 0 || checkYPos >= dataMaxSizeY
            || (checkXPos == inXPos && checkYPos == inYPos)) {
          continue;
        }

        numLiveNeighbours = inData[checkYPos][checkXPos] == 1 ? numLiveNeighbours + 1 : numLiveNeighbours;
      }
    }

    return numLiveNeighbours;

  }
}
