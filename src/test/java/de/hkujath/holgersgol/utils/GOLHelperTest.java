package de.hkujath.holgersgol.utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.hkujath.holgersgol.data.IGOLData;
import de.hkujath.holgersgol.data.impl.GOLData;
import de.hkujath.holgersgol.exceptions.GOLException;

class GOLHelperTest {

  private static int[][] TEST_GRID = new int[][] { { 0, 1, 0 }, { 0, 1, 1 }, { 0, 0, 0 } };

  /**
   * 
   * @throws GOLException
   *
   * @author Kujath, Holger
   * @since 06.10.2021
   */
  @Test
  void testPrint2DArrayAllFine() throws GOLException {
    GOLHelper.print2DArray(TEST_GRID);
    //TODO check log file.
  }

  /**
   * 
   * @throws GOLException
   *
   * @author Kujath, Holger
   * @since 06.10.2021
   */
  @Test
  void testPrint2DArrayException() throws GOLException {
    assertThrows(GOLException.class, () -> GOLHelper.print2DArray(null), "Test failed");

  }

  /**
   * 
   * @throws GOLException
   *
   * @author Kujath, Holger
   * @since 06.10.2021
   */
  @Test
  void testCalcNextGenerationAllFine() throws GOLException {
    IGOLData gridData = new GOLData(3, 3);
    gridData.initializeGrid(TEST_GRID);

    int[][] result = GOLHelper.calcNextGeneration(gridData.getGridData());

    int[][] expectedResult = new int[][] { { 0, 1, 1 }, { 0, 1, 1 }, { 0, 0, 0 } };

    assertTrue(Arrays.deepEquals(expectedResult, result),
        "Arrays are not equal. (expected: " + expectedResult + ") (actual " + result + ")");

  }

  /**
   * 
   * @throws GOLException
   *
   * @author Kujath, Holger
   * @since 06.10.2021
   */
  @Test
  void testCalcNextGenerationEXception() throws GOLException {
    assertThrows(GOLException.class, () -> GOLHelper.calcNextGeneration(null), "Test failed");
  }

  /**
   * 
   * @throws GOLException
   *
   * @author Kujath, Holger
   * @since 05.10.2021
   */
  @Test
  void testClone2DArrayAllFine() throws GOLException {
    int[][] result = GOLHelper.clone2DArray(TEST_GRID);

    assertTrue(Arrays.deepEquals(TEST_GRID, result), "Arrays are not equal.");

  }

  /**
   * 
   * 
   *
   * @author Kujath, Holger
   * @since 05.10.2021
   */
  @Test
  void testClone2DArrayException() {
    assertThrows(GOLException.class, () -> GOLHelper.print2DArray(null), "Test failed");
  }

  /**
   * 
   * @param inData
   * @param xPos
   * @param yPos
   * @throws GOLException
   *
   * @author Kujath, Holger
   * @since 03.10.2021
   */
  @ParameterizedTest
  @MethodSource("calcIllegalLiveNeighboursSource")
  void calcNumLiveNeighboursException(int[][] inData, int xPos, int yPos) throws GOLException {
    assertThrows(GOLException.class, () -> GOLHelper.calcNumLiveNeighbours(inData, xPos, yPos), "Test failed.");

  }

  /**
   * 
   * @throws GOLException
   *
   * @author Kujath, Holger
   * @since 03.10.2021
   */
  @ParameterizedTest
  @MethodSource("calcLegalLiveNeighboursSource")
  void calcNumLiveNeighboursAllFine(int[][] inData, int xPos, int yPos, int expectedResult) throws GOLException {

    int curRresult = GOLHelper.calcNumLiveNeighbours(inData, xPos, yPos);
    assertEquals(expectedResult, curRresult,
        "Number of expected neighbours (" + expectedResult + ") was incorrect (" + curRresult + ").");
  }

  // ############# Helper functions #############################

  /**
   * 
   * @return Illegal number of neighbours
   *
   * @author Kujath, Holger
   * @since 03.10.2021
   */
  private static Stream<Arguments> calcIllegalLiveNeighboursSource() {
    Object testArray = (Object) new int[][] { { 0, 0 }, { 0, 0 } };
    return Stream.of(arguments(testArray, -1, 0), arguments(testArray, 0, -1), arguments(testArray, 999, 1),
        arguments(testArray, 1, 999)

    );
  }

  /**
   * 
   * @return Legal number of neighbours.
   *
   * @author Kujath, Holger
   * @since 03.10.2021
   */
  private static Stream<Arguments> calcLegalLiveNeighboursSource() {

    // 3x3
    Object testArray1 = (Object) new int[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };
    Object testArray2 = (Object) new int[][] { { 0, 1, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };

    // 4x3
    Object testArray3 = (Object) new int[][] { { 0, 0, 0, 0 }, { 1, 1, 1, 0 }, { 0, 0, 0, 0 } };
    Object testArray4 = (Object) new int[][] { { 0, 0, 0, 0 }, { 0, 0, 0, 1 }, { 0, 0, 1, 0 } };

    // 3x4
    Object testArray5 = (Object) new int[][] { { 0, 0, 0 }, { 0, 0, 1 }, { 0, 0, 0 }, { 0, 0, 0 } };
    Object testArray6 = (Object) new int[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 1, 0 } };

    return Stream.of(arguments(testArray1, 0, 0, 0), arguments(testArray2, 0, 0, 1), arguments(testArray3, 2, 1, 1),
        arguments(testArray4, 3, 2, 2), arguments(testArray5, 1, 2, 1), arguments(testArray6, 2, 3, 1)

    );
  }
}
