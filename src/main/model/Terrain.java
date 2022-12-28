package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Represents a map with a width, height, terrain, and units. Map dimensions and
 * terrain can be edited, as well as unit parameters for units that are on the map.
 */
public class Terrain implements Writable {

    private static final Logger log = Logger.getLogger(Terrain.class.getName());

    private String name;
    private TerrainTile[][] terrainTile;

    public static final int MIN_WIDTH = 15;
    public static final int MIN_HEIGHT = 10;

    private final UnitList units;


    /**
     * Constructs a new map, entirely of plain tiles, with the given height and width specifications.
     *
     * @param name   Name of the Terrain
     * @param width  Width of Terrain. Must be greater than MIN_WIDTH
     * @param height Height of Terrain. Must be greater than MIN_HEIGHT
     */

    // TODO ADD EXCEPTION FOR INSUFFICIENT HEIGHT+WIDTH
    public Terrain(String name, int width, int height) {
        this.name = name;
        this.terrainTile = new TerrainTile[width][height];
        setAllTerrainToPlain(terrainTile);
        this.units = new UnitList();
        log.fine(EventUtility.getTerrainInstantiationFromWidthAndHeightMessage(this));
    }

    /**
     * Constructs a new map with the given specifications.
     *
     * @param name        Name for new Terrain
     * @param terrainTile 2D TerrainTile array of new Terrain. Width and Height of map must exceed
     *                    MIN_WIDTH and MIN_HEIGHT, respectively
     * @param units       List of units on the terrain
     */

    // TODO ADD EXCEPTION FOR INSUFFICIENT HEIGHT+WIDTH
    public Terrain(String name, TerrainTile[][] terrainTile, UnitList units) {
        this.name = name;
        this.terrainTile = terrainTile;
        this.units = units;
        log.fine(EventUtility.getTerrainInstantiationFromExistingTerrainMessage(this));
    }

    /**
     * Sets all tiles in the terrain array to plain.
     *
     * @param map The terrain array in question
     */
    private static void setAllTerrainToPlain(TerrainTile[][] map) {
        int width = map.length;
        int height = map[0].length;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map[i][j] = TerrainTile.PLAIN;
            }
        }
        log.fine(EventUtility.getSetAllToPlainMessage());
    }

    /**
     * Returns the width of the map.
     */
    public int getWidth() {
        return terrainTile.length;
    }

    /**
     * Returns the height of the map.
     */
    public int getHeight() {
        return terrainTile[0].length;
    }

    /**
     * Returns the tile type.
     */
    public TerrainTile getTileType(int x, int y) {
        return terrainTile[x][y];
    }

    /**
     * Returns the name of the map.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if there is a unit at the given position.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return true if the position is unoccupied, false otherwise.
     */

    //TODO Add OOB Exception
    private boolean isPositionUnoccupied(int x, int y) {
        for (Unit unit : units) {
            if (unit.getX() == x && unit.getY() == y) {
                return false;
            }
        }
        return true;
    }

    /**
     * Moves the given unit to the given coordinates.
     *
     * @param unit The unit in question
     * @param x    The new X position
     * @param y    The new Y position
     * @return If the move was successful
     */
    public boolean moveUnit(Unit unit, int x, int y) {
        if (isPositionUnoccupied(x, y)) {
            unit.move(x, y);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds unit to unit list and returns true if its tile is unoccupied, returns false otherwise.
     *
     * @param unit The unit to be added
     */
    public boolean addUnit(Unit unit) {
        int unitX = unit.getX();
        int unitY = unit.getY();
        if (unitX < 0 || unitX >= getWidth() || unitY < 0 || unitY >= getHeight() || !isPositionUnoccupied(unit.getX(), unit.getY())) {
            return false;
        } else {
            units.add(unit);
            log.fine(EventUtility.getAddUnitMessage(unitX, unitY));
            return true;
        }
    }

    /**
     * If there is a unit at (x, y), removes them.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return True there was a unit at the given position, false otherwise
     */
    public boolean deleteUnit(int x, int y) {
        for (Unit unit : units) {
            if (unit.getX() == x && unit.getY() == y) {
                units.remove(unit);
                log.fine(EventUtility.getRemoveUnitMessage(x, y));
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
        return name.equals(terrain.name) && Arrays.deepEquals(terrainTile, terrain.terrainTile) && units.equals(terrain.units);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Returns the unit at the given coordinates
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return The unit at the given coordinates, or null if no such unit exists
     */
    public Unit getUnit(int x, int y) {
        for (Unit unit : units) {
            if (x == unit.getX() && y == unit.getY()) {
                return unit;
            }
        }
        return null;
    }

    /**
     * Renames the Terrain
     *
     * @param newName The new name for the map
     */
    public void rename(String newName) {
        this.name = newName;
        log.fine(EventUtility.getRenameMessage(newName));
    }

    /**
     * Resizes the map to match the given parameters
     *
     * @param width  New width of Terrain. Must be greater than MIN_WIDTH
     * @param height New height of Terrain. Must be greater than MIN_HEIGHT
     */

    //TODO Add OOB Exception
    public void resize(int width, int height) {
        TerrainTile[][] newTerrainTile = new TerrainTile[width][height];
        TerrainTile[][] oldTerrainTile = this.terrainTile;
        setAllTerrainToPlain(newTerrainTile);
        int minWidth = Math.min(this.getWidth(), width);
        int minHeight = Math.min(this.getHeight(), height);
        for (int i = 0; i < minWidth; i++) {
            if (minHeight >= 0)
                System.arraycopy(oldTerrainTile[i], 0, newTerrainTile[i], 0, minHeight);
        }
        this.terrainTile = newTerrainTile;
        handleUnits();
        log.fine(EventUtility.getResizeMessage(width, height));
    }

    /**
     * Removes units outside the bounds of the map
     */
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

    /**
     * Changes the terrain tile at the given coordinates to the new type if tile is in bounds and returns true,
     * returns false otherwise
     *
     * @param newTerrainTypeTile The new terrain tile type
     * @param x                  X coordinate
     * @param y                  Y coordinate
     */
    public boolean setTile(TerrainTile newTerrainTypeTile, int x, int y) {
        if (0 <= x && x < getWidth() && 0 <= y && y < getHeight() && terrainTile[x][y] != newTerrainTypeTile) {
            terrainTile[x][y] = newTerrainTypeTile;
            log.fine(EventUtility.getSetTileMessage(newTerrainTypeTile, x, y));
            return true;

        } else {
            return false;
        }
    }

    /**
     * Returns all units on the terrain
     *
     * @return The list of all units
     */
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
        log.fine("Saved terrain " + name + " to JSON");
        return mapJson;
    }

    /**
     * Transforms all columns of a map into a JSON array
     *
     * @return The JSON array of columns
     */
    private JSONArray columnsToJson() {
        JSONArray result = new JSONArray();
        for (TerrainTile[] terrainTileList : this.terrainTile) {
            result.put(columnToJson(terrainTileList));
        }
        return result;
    }

    /**
     * Transforms an individual column into a JSON array
     *
     * @return A JSON array representing one column of terrain
     */
    private static JSONArray columnToJson(TerrainTile[] terrainTileList) {
        JSONArray result = new JSONArray();
        for (TerrainTile terrainTile : terrainTileList) {
            result.put(terrainToJson(terrainTile));
        }
        return result;
    }

    /**
     * Transforms a tile into a JSON object
     *
     * @return A JSON object representing a terrain tile
     */
    private static JSONObject terrainToJson(TerrainTile terrainTile) {
        JSONObject result = new JSONObject();
        result.put("terrain", terrainTile);
        return result;
    }

}
