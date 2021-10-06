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
import de.hkujath.holgersgol.utils.GOLHelper;

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
    int[][] genData = testObj.getGridData();
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
   * @param x
   * @param y
   * @throws GOLException
   */
  @ParameterizedTest
  @CsvSource({ "3,3", "20,10", "8,3", "2,12" })
  void testInitializeGridAllFine(int x, int y) throws GOLException {
    GOLData data = new GOLData(x, y);

    // Check before init
    OptionalInt a = Arrays.stream(data.getGridData()).flatMapToInt(row -> Arrays.stream(row)).filter(val -> {
      return !(0 <= val && val <= 1);
    }).findAny();
    assertFalse(a.isPresent());

    data.initializeGrid();

    // Check after init
    OptionalInt b = Arrays.stream(data.getGridData()).flatMapToInt(row -> Arrays.stream(row)).filter(val -> {
      return !(0 <= val && val <= 1);
    }).findAny();
    assertFalse(b.isPresent());
  }

  /**
   * 
   * @throws GOLException
   */
  @Test
  void testcalcNextGenerationAllFine() throws GOLException {
    GOLData data = new GOLData(5, 3);
    data.initializeGrid();

    int[][] startGrid = data.getGridData();
    GOLHelper.print2DArray(data.getGridData());

    //data.calcNextGeneration();

    GOLHelper.print2DArray(data.getGridData());
  }

}
