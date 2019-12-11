package tss.g2.fyre.models.actions;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.datastorage.DataStorage;

/**
 * Class for do action, end check time.
 */
public class ActionTime implements Action {
  private String api;
  private Action action;
  private DataStorage dataStorage;

  /**
   * Constructor.
   * @param api api for check time
   * @param action action for check time.
   * @param dataStorage data storage.
   */
  public ActionTime(String api, Action action, DataStorage dataStorage) {
    this.api = api;
    this.action = action;
    this.dataStorage = dataStorage;
  }

  @Override
  public Answer getAnswer() {
    long start = System.currentTimeMillis();
    Answer answer = action.getAnswer();
    long end = System.currentTimeMillis();
    long time = end - start;
    //dataStorage.addTimeApi(api, time);
    return answer;
  }
}
