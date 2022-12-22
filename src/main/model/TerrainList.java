package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a list of maps. All methods barring the constructor were modeled after
// methods from the JsonSerializationDemo
public class TerrainList extends ArrayList<Terrain> implements Writable {

    // EFFECTS: Constructs an empty map list. Code is here for clarity
    public TerrainList() {
        super();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("maps", mapsToJason());
        return json;
    }

    //EFFECTS: Returns a JSONArray representation of this
    private JSONArray mapsToJason() {
        JSONArray result = new JSONArray();

        for (Terrain terrain : this) {
            result.put(terrain.toJson());
        }

        return result;
    }
}
