package model;

import org.json.JSONArray;


import java.util.LinkedList;

/**
 * Represents a list of units
 */
public class UnitList extends LinkedList<Unit> {

    /**
     * Transforms the unit list into a JSONArray
     *
     * @return the JSONArray representation of the UnitList
     */
    public JSONArray toJson() {
        JSONArray result = new JSONArray();

        for (Unit unit : this) {
            result.put(unit.toJson());
        }

        return result;
    }
}
