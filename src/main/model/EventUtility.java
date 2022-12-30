package model;

/**
 * A utility class for producing log messages.
 */
public abstract class EventUtility {

    /**
     * Returns a textual representation of a position
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return Textual representation of a position
     */
    public static String positionToString(int x, int y) {
        return "(" + x + "," + y + ")";
    }

    /**
     * Returns the log message for setting all terrain to plain
     * @return message
     */
    public static String getSetAllToPlainMessage() {
        return "Set all terrain to plain";
    }

    /**
     * Returns the log message for instantiating terrain from width and height
     *
     * @param t Terrain in question
     * @return message
     */
    public static String getTerrainInstantiationFromWidthAndHeightMessage(Terrain t) {
        return "Instantiated new terrain with a width of " + t.getWidth()
                + ", a height of " + t.getHeight() + ", and name " + t.getName();
    }

    /**
     * Returns the log message for instantiating terrain from pre-existing parameters
     *
     * @param t Terrain in question
     * @return message
     */
    public static String getTerrainInstantiationFromExistingTerrainMessage(Terrain t) {
        return "Instantiated terrain " + t.getName() + " with given parameters";
    }

    /**
     * Returns the log message for moving a unit
     *
     * @param x new x coordinate
     * @param y new y coordinate
     * @return message
     */
    public static String getMoveUnitMessage(int x, int y) {
        return "Moved unit to " + positionToString(x, y);
    }

    /**
     * Returns the log message for adding a unit
     *
     * @param x unit x coordinate
     * @param y unit y coordinate
     * @return message
     */
    public static String getAddUnitMessage(int x, int y) {
        return "Added unit at " + positionToString(x, y);
    }

    /**
     * Returns the log message for removing a unit
     *
     * @param x removed unit x position
     * @param y removed unit y position
     * @return
     */
    public static String getRemoveUnitMessage(int x, int y) {
        return "Removed unit at " + positionToString(x, y);
    }

    /**
     * Returns the log message for resizing a Terrain
     *
     * @param width new width
     * @param height new height
     * @return message
     */
    public static String getResizeMessage(int width, int height) {
        return "Resized map to " + width + " x " + height;
    }

    /**
     * Returns the log message for setting a TerrainTile on a Terrain
     *
     * @param newTerrainTypeTile the TerrainTile type that was set
     * @param x x coordinate
     * @param y y coordinate
     * @return
     */
    public static String getSetTileMessage(TerrainTile newTerrainTypeTile, int x, int y) {
        return "Set terrain at " + positionToString(x, y) + " to " + newTerrainTypeTile.toString();
    }

    /**
     * Returns the log message for instantiating a Unit
     *
     * @param unit the unit in question
     * @return message
     */
    public static String getUnitInstantiationMessage(Unit unit) {
        return "Instantiated a new unit of faction " + unit.getFaction().toString()
                + ", battle class " + unit.getBattleClass().toString() + ", and position "
                + EventUtility.positionToString(unit.getX(), unit.getY());
    }

    /**
     * Returns the log message for changing a Unit's Faction
     *
     * @param unit the unit in question
     * @return message
     */
    public static String getFactionChangeMessage(Unit unit) {
        return "Changed faction of unit at " + EventUtility.positionToString(unit.getX(), unit.getY())
                + " to " + unit.getFaction().toString();
    }

    /**
     * Returns the log message for changing a Unit's BattleClass
     *
     * @param unit the unit in question
     * @return message
     */
    public static String getBattleClassChangeMessage(Unit unit) {
        return "Changed battle class of unit at " + EventUtility.positionToString(unit.getX(), unit.getY())
                + " to " + unit.getBattleClass().toString();
    }

    /**
     * Returns the log message for changing the name of a Terrain
     *
     * @param newName the Terrain's new name
     * @return message
     */
    public static String getRenameMessage(String newName) {
        return "Renamed terrain to " + newName;
    }

}
