package tss.g2.fyre.models.datastorage;

import tss.g2.fyre.models.entity.Person;

/**
 * This interface describe data storage worker.
 *
 * @author Anton Kudryavtsev
 */
public interface DataStorage {

  /**
   * Get authorization info by login.
   *
   * @param login login
   * @return authorization info
   */
  Person getAuthorization(String login);
}
