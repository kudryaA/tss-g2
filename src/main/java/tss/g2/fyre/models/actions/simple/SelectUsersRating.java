package tss.g2.fyre.models.actions.simple;

import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.Action;
import tss.g2.fyre.models.datastorage.DataStorage;

/**
 * Action class for select  users rating.
 */

public class SelectUsersRating implements Action {
    private DataStorage dataStorage;

    /**
     * Constructor.
     *
     * @param dataStorage data storage object
     */
    public SelectUsersRating(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public Answer getAnswer() {
        return new Answer<>(true,
                dataStorage.selectUsersRating());
    }
}
