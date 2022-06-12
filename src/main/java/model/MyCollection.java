package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Hashtable;

/**
 * the class stores the collection and the date of its initialization
 */
public class MyCollection implements Serializable {
    /**
     * date of its initialization
     */
    private final Date initialization_date;
    /**
     * collection
     */
    private final Hashtable<Integer, Flat> collection;

    /**
     * Initializes a newly created MyCollection  object
     */
    public MyCollection() {
        initialization_date = new Date();
        collection = new Hashtable<>();
    }

    public Hashtable<Integer, Flat> getCollection() {
        return collection;
    }

    public Date getInitialization_date() {
        return initialization_date;
    }
}
