package hu.ait.travelmap;

import hu.ait.travelmap.data.EntryData;

/**
 * Created by caitlinwesterfield on 7/4/17.
 */

public class EntryModel {
    private static EntryModel instance = null;

    public static EntryModel getInstance() {
        if (instance == null) {
            instance = new EntryModel();
        }

        return instance;
    }

    private EntryModel() {
    }

    private EntryData entryData;

    public EntryData getEntryData() {
        return entryData;
    }

    public void setEntryData(EntryData entryData) {
        this.entryData = entryData;
    }
}
