package de.hkujath.holgersgol;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hkujath.holgersgol.utils.IToolVersion;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HolgersGOL extends Application {

  /** logger instance */
  private static final Logger       LOG              = LoggerFactory.getLogger(HolgersGOL.class);

  /** tool information */
  private static final IToolVersion TOOL_INFORMATION = new Version();

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    Stage mainStage = primaryStage;

    Parent rootNode = null;
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(HolgersGOL.class.getResource("/fxml/MainView.fxml"));

      //get root node.
      rootNode = fxmlLoader.load();

      //Set the main view controller.
      MainController mainCtl = fxmlLoader.getController();

      mainCtl.setStage(mainStage);

    } catch (final IOException e) {
      LOG.error("Error during load() command.", e);
    }

    final Scene scene = new Scene(rootNode);

    mainStage.setScene(scene);
    mainStage.setResizable(true);
    mainStage.setTitle("HolgersGOL" + " " + TOOL_INFORMATION.getBuild());
    mainStage.show();

  }

}
