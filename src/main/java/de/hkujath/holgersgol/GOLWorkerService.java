package de.hkujath.holgersgol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hkujath.holgersgol.data.IGOLData;
import de.hkujath.holgersgol.exceptions.GOLException;
import de.hkujath.holgersgol.utils.GOLHelper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class GOLWorkerService extends Service<Void> {

  /** grid data */
  private IGOLData              gameData;

  /** Number of generations */
  private int                   numGenerations;

  private SimpleIntegerProperty currenGeneration;

  /** logger instance */
  private static final Logger   LOG = LoggerFactory.getLogger(GOLWorkerService.class);

  public GOLWorkerService(IGOLData inData, int inNumGens) throws GOLException {

    if (inData == null || inNumGens < 1) {
      throw new GOLException("Given input data is invalid.");
    }
    this.currenGeneration = new SimpleIntegerProperty(-1);
    this.numGenerations = inNumGens;
    this.gameData = inData;
  }

  /**
   * 
   * @return current generation property
   *
   * @author Kujath, Holger
   * @since 05.10.2021
   */
  public SimpleIntegerProperty currenGenerationProperty() {
    return this.currenGeneration;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javafx.concurrent.Service#createTask()
   */
  @Override
  protected Task<Void> createTask() {

    LOG.debug("Creating task...");
    return new Task<Void>()
    {
      /*
       * (non-Javadoc)
       * 
       * @see javafx.concurrent.Task#call()
       */
      @Override
      protected Void call() throws Exception {

        try {

          LOG.debug("Init grid.");
          // Init grid
          gameData.initializeGrid();

          LOG.debug("Starting game of life....");
          currenGeneration.set(0);

          for (int i = 1; i <= numGenerations; i++) {
            try {
              Thread.sleep(1000);
            } catch (InterruptedException e) {
              LOG.trace(e.getLocalizedMessage());
            }
            gameData.setGridData(GOLHelper.calcNextGeneration(gameData.getGridData()));
            LOG.debug("Generation {} calculated.", i);
            currenGeneration.set(i);
          }

          LOG.debug("Finished game of life....");

          return null;

        } catch (Exception e) {
          LOG.error("Can't play the game. Reason: {}", e.getLocalizedMessage());
          return null;
        }
      }
    };
  }

  /**
   * 
   * @return the IGOLData object.
   *
   * @author Kujath, Holger
   * @since 04.10.2021
   */
  public IGOLData getGOLData() {
    return this.gameData;
  }

}
