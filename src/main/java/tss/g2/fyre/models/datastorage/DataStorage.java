package tss.g2.fyre.models.datastorage;

import tss.g2.fyre.models.entity.Authorization;
import tss.g2.fyre.models.entity.Moderator;

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
  Authorization getAuthorization(String login);

  /**
   * Get moderator by login.
   *
   * @param login login
   * @return moderator
   */
  Moderator getModerator(String login);
}
