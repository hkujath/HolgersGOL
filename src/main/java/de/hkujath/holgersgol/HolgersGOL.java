package de.hkujath.holgersgol;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hkujath.holgersgol.utils.IToolVersion;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class HolgersGOL extends Application {

  /** main stage */
  private Stage               mainStage;

  private MainController  mainCtl;
  
  /** logger instance */
  private static final Logger LOG = LoggerFactory.getLogger(HolgersGOL.class);
  
  /** tool information */
  private final static IToolVersion TOOL_INFORMATION = new Version();


  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    //    String javaVersion = System.getProperty("java.version");
    //    String javafxVersion = System.getProperty("javafx.version");
    //    Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
    //    Scene scene = new Scene(new StackPane(l), 640, 480);
    //    primaryStage.setScene(scene);
    //    primaryStage.show();

    this.mainStage = primaryStage;

    Parent rootNode = null;
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(HolgersGOL.class.getResource("/fxml/MainView.fxml"));

      //get root node.
      rootNode = fxmlLoader.load();
      
      //Set the main view controller.
      this.mainCtl = fxmlLoader.getController();

    } catch (final IOException e) {
      LOG.error("Error during load() command.", e);
    }
    
    final Scene scene = new Scene(rootNode);
    //scene.getStylesheets().add(DerStemplerMain.getStyleSheetURL());

    //Set event handler for startup
    //mainStage.addEventHandler(WindowEvent.WINDOW_SHOWN, mainView.getStartupEventHandler());

    //Set application icon
    //mainStage.getIcons().add(new Image(DerStemplerMain.class.getResourceAsStream("/images/application.png")));

    mainStage.setScene(scene);
    mainStage.setResizable(false);
    mainStage.setTitle("HolgersGOL" + " " + TOOL_INFORMATION.getBuild());
    mainStage.show();


  }

}
