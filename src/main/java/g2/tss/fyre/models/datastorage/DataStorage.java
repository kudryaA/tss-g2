package g2.tss.fyre.models.datastorage;

import g2.tss.fyre.models.entity.Authorization;

/**
 * This interface describe data storage worker
 * @author Anton Kudryavtsev
 */
public interface DataStorage {
    /**
     * Get authorization info by login
     * @param login login
     * @return authorization info
     */
    Authorization getAuthorization(String login);
}
