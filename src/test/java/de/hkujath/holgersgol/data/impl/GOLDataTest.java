package de.hkujath.holgersgol.data.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.OptionalInt;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import de.hkujath.holgersgol.exceptions.GOLException;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class GOLDataTest {

  /**
   * 
   * @throws GOLException
   *
   * @author Kujath, Holger
   * @since 02.10.2021
   */
  @Test
  void testConstructorAllFine() throws GOLException {
    GOLData testObj = new GOLData(2, 3);
    int[][] genData = testObj.getGenerationData();
    assertNotNull(genData);

    int[][] expected = { { 0, 0 }, { 0, 0 }, { 0, 0 } };
    assertArrayEquals(expected, genData);
  }

  /**
   * 
   * @param x size of x dimension
   * @param y size of y dimension
   * @throws GOLException
   *
   * @author Kujath, Holger
   * @since 02.10.2021
   */
  @ParameterizedTest
  @CsvSource({ "0, 0", "-1, 1", "1, 0", "1, -1" })
  void testConstructorException(int x, int y) throws GOLException {
    assertThrows(GOLException.class, () -> new GOLData(x, y), "Test failed");
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
    assertThrows(GOLException.class, () -> GOLData.calcNumLiveNeighbours(inData, xPos, yPos), "Test failed.");

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

    int curRresult = GOLData.calcNumLiveNeighbours(inData, xPos, yPos);
    assertEquals(expectedResult, curRresult,
        "Number of expected neighbours (" + expectedResult + ") was incorrect (" + curRresult + ").");
  }

  @ParameterizedTest
  @CsvSource({ "3,3", "20,10", "8,3", "2,12" })
  void testInitializeGridAllFine(int x, int y) throws GOLException {
    GOLData data = new GOLData(x, y);
    
   
    //Check before init
    OptionalInt a = Arrays.stream(data.getGenerationData()).flatMapToInt(row -> Arrays.stream(row)).filter(val -> {
      return !(0 <= val && val <= 1);
    }).findAny();
    assertFalse(a.isPresent());
    
    data.initializeGrid();
    
  //Check after init
    OptionalInt b = Arrays.stream(data.getGenerationData()).flatMapToInt(row -> Arrays.stream(row)).filter(val -> {
      return !(0 <= val && val <= 1);
    }).findAny();
    assertFalse(b.isPresent());
  }
  
  @Test
  void testcalcNextGenerationAllFine() throws GOLException {
    GOLData data = new GOLData(5,3);
    data.initializeGrid();
    
    int[][] startGrid= GOLData.clone2DArray(data.getGenerationData());
    GOLData.printGrid(startGrid);
    
    data.calcNextGeneration();
    
    GOLData.printGrid(data.getGenerationData());
  
    int a = 3;
  }

  //############# Helper functions #############################

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

    //3x3
    Object testArray1 = (Object) new int[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };
    Object testArray2 = (Object) new int[][] { { 0, 1, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };

    //4x3
    Object testArray3 = (Object) new int[][] { { 0, 0, 0, 0 }, { 1, 1, 1, 0 }, { 0, 0, 0, 0 } };
    Object testArray4 = (Object) new int[][] { { 0, 0, 0, 0 }, { 0, 0, 0, 1 }, { 0, 0, 1, 0 } };

    //3x4
    Object testArray5 = (Object) new int[][] { { 0, 0, 0 }, { 0, 0, 1 }, { 0, 0, 0 }, { 0, 0, 0 } };
    Object testArray6 = (Object) new int[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 }, { 0, 1, 0 } };

    return Stream.of(arguments(testArray1, 0, 0, 0), arguments(testArray2, 0, 0, 1), arguments(testArray3, 2, 1, 1),
        arguments(testArray4, 3, 2, 2), arguments(testArray5, 1, 2, 1), arguments(testArray6, 2, 3, 1)

    );
  }

}
