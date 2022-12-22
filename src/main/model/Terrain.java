package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// Represents a map with a width, height, terrain, and units. Map dimensions and terrain can be edited, as well as unit
// parameters for units that are on the map.
public class Terrain implements Writable {

    private String name;
    private TerrainTile[][] terrainTile;

    public static final int MIN_WIDTH = 15;
    public static final int MIN_HEIGHT = 10;

    private UnitList units;


    //REQUIRES: width >= MIN_WIDTH and height >= MIN_HEIGHT
    //EFFECTS : Constructs a new map, entirely of plain tiles, with the given height and width specifications
    public Terrain(String name, int width, int height) {
        this.name = name;
        this.terrainTile = new TerrainTile[width][height];
        setAllTerrainToPlain(terrainTile);
        this.units = new UnitList();
        EventLog.getInstance().logEvent(new Event(
                EventUtility.getTerrainInstantiationFromWidthAndHeightMessage(this)));
    }

    //REQUIRES: terrain.size >= MIN_WIDTH and terrain[0].size >= MIN_HEIGHT
    //EFFECTS : Constructs a new map, entirely of plain tiles, with the given height and width specifications
    public Terrain(String name, TerrainTile[][] terrainTile, UnitList units) {
        this.name = name;
        this.terrainTile = terrainTile;
        this.units = units;
        EventLog.getInstance().logEvent(new Event(
                EventUtility.getTerrainInstantiationFromExistingTerrainMessage(this)));
    }

    //EFFECTS: Sets all tiles in the terrain array to plain.
    private static void setAllTerrainToPlain(TerrainTile[][] map) {
        int width = map.length;
        int height = map[0].length;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map[i][j] = TerrainTile.PLAIN;
            }
        }
        EventLog.getInstance().logEvent(new Event(EventUtility.getSetAllToPlainMessage()));
    }

    // EFFECTS: Returns the width of the map
    public int getWidth() {
        return terrainTile.length;
    }

    // EFFECTS: Returns the height of the map
    public int getHeight() {
        return terrainTile[0].length;
    }

    // EFFECTS: Returns the tile type
    public TerrainTile getTileType(int x, int y) {
        return terrainTile[x][y];
    }

    // EFFECTS: Returns the name of the map
    public String getName() {
        return name;
    }

    // REQUIRES: 0 <= x < map.getWidth(), 0 <= y < this.getHeight()
    // EFFECTS: Checks if there is a unit at the given position
    private boolean isPositionUnoccupied(int x, int y) {
        for (Unit unit : units) {
            if (unit.getX() == x && unit.getY() == y) {
                return false;
            }
        }
        return true;
    }

    // REQUIRES: 0 <= x < map.getWidth(), 0 <= y < this.getHeight()
    // MODIFIES: this
    // EFFECTS: Moves the given unit to the given coordinates and returns true,
    //          does nothing and returns false if new coordinates are occupied.
    public boolean moveUnit(Unit unit, int x, int y) {
        if (isPositionUnoccupied(x, y)) {
            unit.move(x, y);
            return true;
        } else {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds unit to unit list and returns true if its tile is unoccupied, returns false otherwise.
    public boolean addUnit(Unit unit) {
        int unitX = unit.getX();
        int unitY = unit.getY();
        if (unitX < 0 || unitX >= getWidth() || unitY < 0 || unitY >= getHeight()
                || !isPositionUnoccupied(unit.getX(), unit.getY())) {
            return false;
        } else {
            units.add(unit);
            EventLog.getInstance().logEvent(new Event(EventUtility.getAddUnitMessage(unitX, unitY)));
            return true;
        }
    }

    // REQUIRES: 0 <= x < map.getWidth(), 0 <= y < this.getHeight()
    // MODIFIES: this
    // EFFECTS : If there is a unit at (x, y), removes them. Does nothing if no unit is at the position
    public boolean deleteUnit(int x, int y) {
        for (Unit unit : units) {
            if (unit.getX() == x && unit.getY() == y) {
                units.remove(unit);
                EventLog.getInstance().logEvent(new Event(EventUtility.getRemoveUnitMessage(x, y)));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Terrain terrain = (Terrain) o;
        return name.equals(terrain.name)
                && Arrays.deepEquals(terrainTile, terrain.terrainTile) && units.equals(terrain.units);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    // REQUIRES: 0 <= x < map.getWidth(), 0 <= y < this.getHeight()
    // EFFECTS : If there is a unit at (x, y), returns them. Returns null if no unit is at the position
    public Unit getUnit(int x, int y) {
        for (Unit unit : units) {
            if (x == unit.getX() && y == unit.getY()) {
                return unit;
            }
        }
        return null;
    }

    // MODIFIES: this
    // EFFECTS: renames the map.
    public void rename(String newName) {
        this.name = newName;
        EventLog.getInstance().logEvent(new Event(EventUtility.getRenameMessage(newName)));
    }

    // MODIFIES: this
    // REQUIRES: width >= MIN_WIDTH and height >= MIN_HEIGHT
    // EFFECTS : resizes the map to match the given parameters
    public void resize(int width, int height) {
        TerrainTile[][] newTerrainTile = new TerrainTile[width][height];
        TerrainTile[][] oldTerrainTile = this.terrainTile;
        setAllTerrainToPlain(newTerrainTile);
        int minWidth = Math.min(this.getWidth(), width);
        int minHeight = Math.min(this.getHeight(), height);
        for (int i = 0; i < minWidth; i++) {
            for (int j = 0; j < minHeight; j++) {
                newTerrainTile[i][j] = oldTerrainTile[i][j];
            }
        }
        this.terrainTile = newTerrainTile;
        handleUnits();
        EventLog.getInstance().logEvent(new Event(EventUtility.getResizeMessage(width, height)));
    }

    // MODIFIES: this
    // EFFECTS : removes units outside the bounds of the map
    private void handleUnits() {
        int i = 0;
        while (i < units.size()) {
            Unit unit = units.get(i);
            if (unit.getX() >= this.getWidth() || unit.getY() >= this.getHeight()) {
                this.units.remove(unit);
            } else {
                i++;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS : changes the terrain at the given coordinates to the new type if tile is in bounds and returns true,
    //           returns false otherwise
    public boolean setTile(TerrainTile newTerrainTypeTile, int x, int y) {
        if (0 <= x && x < getWidth() && 0 <= y && y < getHeight() && terrainTile[x][y] != newTerrainTypeTile) {
            terrainTile[x][y] = newTerrainTypeTile;
            EventLog.getInstance().logEvent(new Event(EventUtility.getSetTileMessage(newTerrainTypeTile, x, y)));
            return true;

        } else {
            return false;
        }
    }

    public List<Unit> getUnits() {
        return units;
    }


    //All methods beyond this point were modeled after methods from the Workroom class
    //https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONObject mapJson = new JSONObject();
        mapJson.put("name", this.name);
        mapJson.put("terrainFull", columnsToJson());
        mapJson.put("units", units.toJson());
        EventLog.getInstance().logEvent(new Event("Saved terrain " + name + " to JSON"));
        return mapJson;
    }

    // EFFECTS: Transforms all columns of a map into a JSON array
    private JSONArray columnsToJson() {
        JSONArray result = new JSONArray();
        for (TerrainTile[] terrainTileList : this.terrainTile) {
            result.put(columnToJson(terrainTileList));
        }
        return result;
    }

    // EFFECTS: Transforms an individual column into a JSON array
    private static JSONArray columnToJson(TerrainTile[] terrainTileList) {
        JSONArray result = new JSONArray();
        for (TerrainTile terrainTile : terrainTileList) {
            result.put(terrainToJson(terrainTile));
        }
        return result;
    }

    // EFFECTS: Transforms a tile into a JSON object
    private static JSONObject terrainToJson(TerrainTile terrainTile) {
        JSONObject result = new JSONObject();
        result.put("terrain", terrainTile);
        return result;
    }

}
