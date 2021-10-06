package de.hkujath.holgersgol;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hkujath.holgersgol.data.IGOLData;
import de.hkujath.holgersgol.data.impl.GOLData;
import de.hkujath.holgersgol.exceptions.GOLException;
import de.hkujath.holgersgol.utils.GOLHelper;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Alert.AlertType;

public class MainController implements Initializable {

  @FXML
  private Button              btnStart;

  @FXML
  private TextField           tfXValue;

  @FXML
  private TextField           tfYValue;

  @FXML
  private TextField           tfNumGens;

  @FXML
  private Canvas              cDrawingArea;

  @FXML
  private Label               lblGenNumber;

  @FXML
  private AnchorPane          apMainWindow;

  GOLWorkerService            service;

  private Stage               mainStage      = null;

  /** logger instance */
  private static final Logger LOG            = LoggerFactory.getLogger(MainController.class);

  /***/
  private static final double DEFAULT_WIDTH  = 20;
  /***/
  private static final double DEFAULT_HEIGHT = 20;
  /***/
  private static final double DEFAULT_SPACE  = 10;

  /**
   * Default constructor
   * 
   *
   * @author Kujath, Holger
   * @since 05.10.2021
   */
  public MainController() {

  }

  /**
   * 
   * @param mainStage the main stage of the app.
   *
   * @author Kujath, Holger
   * @since 05.10.2021
   */
  public void setStage(Stage mainStage) {
    this.mainStage = mainStage;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Empty
  }

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

    // Validate number of generations to calculate.
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

    // Validate grid dimensions.
    GOLData gameData = checkInputData(strXVal, strYVal);
    if (gameData == null) {
      showDialog(AlertType.ERROR, "Can't play the game. Given dimensions are invalid.");
      return;
    }

    //Start the service
    try {

      if (service != null && service.isRunning()) {
        service.cancel();
      }

      service = new GOLWorkerService(gameData, numGens);

      service.currenGenerationProperty().addListener((observable, oldValue, newValue) -> {
        LOG.debug("Valued Changed. Current generation: {}", newValue);

        Platform.runLater(() -> {
          lblGenNumber.setText("Generation " + newValue);
          try {
            drawShapes(service.getGOLData().getGridData());
          } catch (GOLException e) {
            LOG.error("Can update GUI with data grid information.");
          }
        });

      });

    } catch (GOLException e) {
      LOG.error("Start of the GOLService failed. Reason: {}", e.getLocalizedMessage());
    }

    LOG.debug("Starting GOLWorkerService...");
    service.start();

  }

  /**
   * Function is used to print a generation
   * 
   * @param genNumber
   * @param inData
   *
   * @author Kujath, Holger
   * @since 04.10.2021
   */
  private static void printGeneration(int genNumber, GOLData inData) {
    LOG.debug("Grid of generation {}", genNumber);
    try {
      GOLHelper.print2DArray(inData.getGridData());
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
      return null;
    }

    int yVal;
    try {
      yVal = Integer.parseInt(strYVal);
    } catch (NumberFormatException e) {
      LOG.error("Cant parse y value [{}]", strXVal);
      return null;
    }

    GOLData gameData;
    try {
      gameData = new GOLData(xVal, yVal);
    } catch (GOLException e) {
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
   * @since 05.10.2021
   */
  private void drawShapes(int[][] inGridData) throws GOLException {

    if (inGridData == null) {
      throw new GOLException("Given grid data is invalid.");
    }

    int dataMaxSizeX = inGridData[0].length;
    int dataMaxSizeY = inGridData.length;

    // Calc generations
    GraphicsContext gc = cDrawingArea.getGraphicsContext2D();

    cDrawingArea.setWidth(dataMaxSizeX * DEFAULT_WIDTH + dataMaxSizeX * DEFAULT_SPACE);
    cDrawingArea.setHeight(dataMaxSizeY * DEFAULT_HEIGHT + dataMaxSizeY * DEFAULT_SPACE);

    gc.clearRect(0, 0, cDrawingArea.getWidth(), cDrawingArea.getHeight());

    gc.setFill(Color.BLACK);
    gc.setStroke(Color.BLUE);

    for (int yPos = 0; yPos < dataMaxSizeY; yPos++) {

      for (int xPos = 0; xPos < dataMaxSizeX; xPos++) {
        boolean isCellAlive = inGridData[yPos][xPos] == 1;

        double drawinvXPos = xPos * DEFAULT_WIDTH + DEFAULT_SPACE * xPos;
        double drawingYPos = yPos * DEFAULT_HEIGHT + DEFAULT_SPACE * yPos;

        if (isCellAlive) {
          gc.fillRect(drawinvXPos, drawingYPos, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        } else {
          gc.strokeRect(drawinvXPos, drawingYPos, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
      }
    }

    mainStage.sizeToScene();

  }

}
