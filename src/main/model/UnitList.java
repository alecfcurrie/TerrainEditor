package model;

import org.json.JSONArray;


import java.util.LinkedList;

public class UnitList extends LinkedList<Unit> {

    // EFFECTS: Transforms the units array into a JSON array
    public JSONArray toJson() {
        JSONArray result = new JSONArray();

        for (Unit unit : this) {
            result.put(unit.toJson());
        }

        return result;
    }
}
