package tss.g2.fyre.models.actions.simple.service;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.Action;
import tss.g2.fyre.models.datastorage.DataStorage;

/**
 * Store time of api.
 */
public class StoreTime implements Action {

  private String api;
  private long time;
  private DataStorage dataStorage;

  /**
   * Constructor.
   * @param dataStorage data storage
   * @param api api
   * @param time tim
   */
  public StoreTime(DataStorage dataStorage, String api, long time) {
    this.api = api;
    this.time = time;
    this.dataStorage = dataStorage;
  }

  @Override
  public Answer getAnswer() {
    return new Answer(dataStorage.addTimeApi(api, time));
  }
}
