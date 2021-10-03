package de.hkujath.holgersgol;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hkujath.holgersgol.data.impl.GOLData;
import de.hkujath.holgersgol.exceptions.GOLException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.control.Alert.AlertType;

public class MainController {

  @FXML
  private transient Button    btnStart;

  @FXML
  private transient TextField tfXValue;

  @FXML
  private transient TextField tfYValue;

  @FXML
  private transient TextField tfNumGens;

  @FXML
  private transient VBox      vbPrintArea;

  /** logger instance */
  private static final Logger LOG            = LoggerFactory.getLogger(MainController.class);

  /***/
  private static final double DEFAULT_WIDTH  = 20;
  /***/
  private static final double DEFAULT_HEIGHT = 20;
  /***/
  private static final double DEFAULT_SPACE  = 10;

  /**
   * 
   * 
   *
   * @author Kujath, Holger
   * @since 01.10.2021
   */
  @FXML
  protected void startGOL() {

    String strXVal = tfXValue.getText();
    String strYVal = tfYValue.getText();
    String strNumGens = tfNumGens.getText();

    //Validate number of generations to calculate.
    int numGens;
    try {
      numGens = Integer.parseInt(strNumGens);
    } catch (NumberFormatException e) {
      showDialog(AlertType.ERROR, "Can't play the game. Number of generations is invalid.");
      return;
    }

    if (numGens < 1) {
      showDialog(AlertType.ERROR, "Can't play the game. Number of generations is < 1.");
      return;
    }

    //Validate grid dimensions.
    GOLData gameData = checkInputData(strXVal, strYVal);
    if (gameData == null)
      return;

    //Init grid
    try {
      gameData.initializeGrid();
    } catch (GOLException e) {
      showDialog(AlertType.ERROR, "Can't play the game. Reason: " + e.getLocalizedMessage());
      return;
    }

    LOG.debug("Starting game of life....");
    printGeneration(0, gameData.getGenerationData());

    //Calc the pane size
    //    pPrintArea.setMinSize(gameData.getSizeX() * DEFAULT_WIDTH + gameData.getSizeX() * DEFAULT_SPACE,
    //        gameData.getSizeY() * DEFAULT_HEIGHT + gameData.getSizeY() * DEFAULT_SPACE);

    //Calc generations

    try {
      for (int i = 0; i < numGens; i++) {
        gameData.calcNextGeneration();
        printGeneration(i + 1, gameData.getGenerationData());
        LOG.debug("");
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
        }
      }

    } catch (GOLException e) {
      showDialog(AlertType.ERROR, "Can't play the game. Reason: " + e.getLocalizedMessage());
      return;
    }

    //printGeneration(numGens, gameData.getGenerationData());
    LOG.debug("Finished game of life....");
  }

  private static void printGeneration(int genNumber, int[][] inData) {
    LOG.debug("Grid of generation {}", genNumber);
    try {
      GOLData.printGrid(inData);
    } catch (GOLException e) {
      LOG.debug("Failed to print grid data.");
    }
  }

  /**
   * Validate input data.
   * 
   * @param strXVal
   * @param strYVal
   * @return
   *
   * @author Kujath, Holger
   * @since 03.10.2021
   */
  private GOLData checkInputData(String strXVal, String strYVal) {
    int xVal;
    try {
      xVal = Integer.parseInt(strXVal);
    } catch (NumberFormatException e) {
      LOG.error("Cant parse x value [{}]", strXVal);
      showDialog(AlertType.ERROR, "Can't play the game. X dimension is invalid.");
      return null;
    }

    int yVal;
    try {
      yVal = Integer.parseInt(strYVal);
    } catch (NumberFormatException e) {
      LOG.error("Cant parse y value [{}]", strXVal);
      showDialog(AlertType.ERROR, "Can't play the game. Y dimension is invalid.");
      return null;
    }

    GOLData gameData;
    try {
      gameData = new GOLData(xVal, yVal);

    } catch (GOLException e) {
      showDialog(AlertType.ERROR, "Can't play the game. Given dimensions are invalid.");
      return null;
    }

    return gameData;
  }

  /**
   * Shows a dialog.
   * 
   * @param inType dialog type.
   * @param text dialog text.
   *
   * @author Kujath, Holger
   * @since 03.10.2021
   */
  private void showDialog(AlertType inType, String text) {
    Alert alert = new Alert(inType);
    alert.setTitle(inType.name());
    alert.setContentText(text);
    alert.showAndWait();
  }

  /**
   * 
   * @param inGridData
   * @throws GOLException
   *
   * @author Kujath, Holger
   * @since 03.10.2021
   */
  private static List<Rectangle> calc2DGridData(int[][] inGridData) throws GOLException {
    if (inGridData == null) {
      throw new GOLException("Given grid data is invalid.");
    }

    int dataMaxSizeX = inGridData[0].length;
    int dataMaxSizeY = inGridData.length;

    List<Rectangle> rects = new ArrayList<>();
    for (int yPos = 0; yPos < dataMaxSizeY; yPos++) {

      for (int xPos = 0; xPos < dataMaxSizeX; xPos++) {

        Rectangle rectangle = new Rectangle(xPos * DEFAULT_WIDTH + DEFAULT_SPACE * xPos,
            yPos * DEFAULT_HEIGHT + DEFAULT_SPACE * yPos, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        boolean isCellAlive = inGridData[yPos][xPos] == 1;

        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(isCellAlive ? Color.BLACK : Color.WHITE);
        rects.add(rectangle);
      }
    }

    return rects;
  }

}
