package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Reader that interprets information stored in JSON files.
 * Code modeled after methods from the JsonReader class in:
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
public class JsonReader {
    private String source;

    /**
     * Constructs reader to read from source file
     *
     * @param source the path where the file to be read is located, including the filename and extension.
     */
    public JsonReader(String source) {
        this.source = source;
    }

    /**
     * Parses Terrain from source
     *
     * @return parsed Terrain
     * @throws IOException if there is a read error
     */
    public Terrain readTerrain() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseTerrain(jsonObject);
    }

    /**
     * Reads terrain from source, and returns it as a String
     *
     * @param source path of the file trying to be read, including filename and extension
     * @return string representation of source file
     * @throws IOException if there is a read error
     */
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    /**
     * Parses terrain from given JSONObject
     *
     * @param jsonObject terrain represented as a JSONObject
     * @return parsed Terrain
     */
    private Terrain parseTerrain(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        TerrainTile[][] terrainTile = parseTerrainMap(jsonObject.getJSONArray("terrainFull"));
        UnitList units = parseUnits(jsonObject.getJSONArray("units"));
        return new Terrain(name, terrainTile, units);
    }

    /**
     * Parses units from JSON array and returns a list of Units.
     *
     * @param unitsJson JSONArray of units
     * @return parsed UnitList
     */
    private UnitList parseUnits(JSONArray unitsJson) {
        UnitList result = new UnitList();
        for (int i = 0; i < unitsJson.length(); i++) {
            result.add(parseUnit(unitsJson.getJSONObject(i)));
        }
        return result;
    }

    /**
     * Parses a unit from JSON object and returns it.
     *
     * @param jsonObject JSONObject representation of a Unit
     * @return parsed Unit
     */
    private Unit parseUnit(JSONObject jsonObject) {
        int x = jsonObject.getInt("x");
        int y = jsonObject.getInt("y");
        Faction faction = Faction.valueOf(jsonObject.getString("faction"));
        BattleClass battleClass = BattleClass.valueOf(jsonObject.getString("class"));
        return new Unit(faction, battleClass, x, y);
    }

    /**
     * Parses all TerrainTiles from JSON Array and returns it as a 2D TerrainTile array.
     *
     * @param terrainJson 2D Array of TerrainTiles represented as a 2D JSONArray
     * @return parsed 2D TerrainTile array
     */
    private TerrainTile[][] parseTerrainMap(JSONArray terrainJson) {
        int width = terrainJson.length();
        int height = ((JSONArray) terrainJson.get(0)).length();
        TerrainTile[][] terrainTileArray = new TerrainTile[width][height];
        for (int i = 0; i < width; i++) {
            terrainTileArray[i] = parseTerrainColumn((JSONArray) terrainJson.get(i), height);
        }
        return terrainTileArray;
    }

    /**
     * Parses a column of terrain from JSON Array to TerrainTile array.
     *
     * @param terrainColumn column of TerrainTiles represented as a JSONArray
     * @param height height of terrain column
     * @return parsed TerrainTile column
     */
    private TerrainTile[] parseTerrainColumn(JSONArray terrainColumn, int height) {
        TerrainTile[] result = new TerrainTile[height];
        for (int j = 0; j < height; j++) {
            result[j] = TerrainTile.valueOf(terrainColumn.getJSONObject(j).getString("terrain"));
        }
        return result;
    }
}
