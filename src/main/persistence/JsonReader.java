package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Reader that interprets information stored in JSON files.
// Code modeled after methods from the JsonReader class in:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads terrain list from file and returns it;
    // throws IOException if an error occurs reading data from file
    public TerrainList readTerrainList() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseTerrainList(jsonObject);
    }

    public Terrain readTerrain() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseTerrain(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses terrain list from JSON object and returns it
    private TerrainList parseTerrainList(JSONObject jsonObject) {
        TerrainList terrainList = new TerrainList();
        addTerrains(terrainList, jsonObject);
        return terrainList;
    }

    // MODIFIES: terrainList
    // EFFECTS: parses terrains from terrainList JSON object and adds them to the terrainList object
    private void addTerrains(TerrainList terrainList, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("maps");
        for (Object json : jsonArray) {
            JSONObject nextTerrain = (JSONObject) json;
            addTerrain(terrainList, nextTerrain);
        }
    }

    // MODIFIES: terrainList
    // EFFECTS: parses terrains from JSON object and adds it to terrainList
    private void addTerrain(TerrainList terrainList, JSONObject jsonObject) {
        Terrain terrainToAdd = parseTerrain(jsonObject);
        terrainList.add(terrainToAdd);
    }

    private Terrain parseTerrain(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        TerrainTile[][] terrainTile = parseTerrainMap(jsonObject.getJSONArray("terrainFull"));
        UnitList units = parseUnits(jsonObject.getJSONArray("units"));
        return new Terrain(name, terrainTile, units);
    }

    // EFFECTS: Parses units from JSON array and returns a list of Units.
    private UnitList parseUnits(JSONArray unitsJson) {
        UnitList result = new UnitList();
        for (int i = 0; i < unitsJson.length(); i++) {
            result.add(parseUnit(unitsJson.getJSONObject(i)));
        }
        return result;
    }

    // EFFECTS: Parses a unit from JSON object and returns it.
    private Unit parseUnit(JSONObject jsonObject) {
        int x = jsonObject.getInt("x");
        int y = jsonObject.getInt("y");
        Faction faction = Faction.valueOf(jsonObject.getString("faction"));
        BattleClass battleClass = BattleClass.valueOf(jsonObject.getString("class"));
        return new Unit(faction, battleClass, x, y);
    }

    // EFFECTS: Parses all terrain from JSON Array and returns it as a 2D Terrain array.
    private TerrainTile[][] parseTerrainMap(JSONArray terrainJson) {
        int width = terrainJson.length();
        int height = ((JSONArray) terrainJson.get(0)).length();
        TerrainTile[][] terrainTileArray = new TerrainTile[width][height];
        for (int i = 0; i < width; i++) {
            terrainTileArray[i] = parseTerrainColumn((JSONArray) terrainJson.get(i), height);
        }
        return terrainTileArray;
    }

    // EFFECTS: Parses a column of terrain from JSON Array to Terrain array.
    private TerrainTile[] parseTerrainColumn(JSONArray terrainColumn, int height) {
        TerrainTile[] result = new TerrainTile[height];
        for (int j = 0; j < height; j++) {
            result[j] = TerrainTile.valueOf(terrainColumn.getJSONObject(j).getString("terrain"));
        }
        return result;
    }
}
