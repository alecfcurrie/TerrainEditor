package model;

public abstract class EventUtility {

    public static String positionToString(int x, int y) {
        return "(" + x + "," + y + ")";
    }

    public static String getSetAllToPlainMessage() {
        return "Set all terrain to plain";
    }

    public static String getTerrainInstantiationFromWidthAndHeightMessage(Terrain t) {
        return "Instantiated new terrain with a width of " + t.getWidth()
                + ", a height of " + t.getHeight() + ", and name " + t.getName();
    }

    public static String getTerrainInstantiationFromExistingTerrainMessage(Terrain t) {
        return "Instantiated terrain " + t.getName() + " with given parameters";
    }

    public static String getMoveUnitMessage(int x, int y) {
        return "Moved unit to " + positionToString(x, y);
    }

    public static String getAddUnitMessage(int x, int y) {
        return "Added unit at " + positionToString(x, y);
    }

    public static String getRemoveUnitMessage(int x, int y) {
        return "Removed unit at " + positionToString(x, y);
    }

    public static String getResizeMessage(int width, int height) {
        return "Resized map to " + width + " x " + height;
    }

    public static String getSetTileMessage(TerrainTile newTerrainTypeTile, int x, int y) {
        return "Set terrain at " + positionToString(x, y) + " to " + newTerrainTypeTile.toString();
    }

    public static String getUnitInstantiationMessage(Unit unit) {
        return "Instantiated a new unit of faction " + unit.getFaction().toString()
                + ", battle class " + unit.getBattleClass().toString() + ", and position "
                + EventUtility.positionToString(unit.getX(), unit.getY());
    }

    public static String getFactionChangeMessage(Unit unit) {
        return "Changed faction of unit at " + EventUtility.positionToString(unit.getX(), unit.getY())
                + " to " + unit.getFaction().toString();
    }

    public static String getBattleClassChangeMessage(Unit unit) {
        return "Changed battle class of unit at " + EventUtility.positionToString(unit.getX(), unit.getY())
                + " to " + unit.getBattleClass().toString();
    }

    public static String getRenameMessage(String newName) {
        return "Renamed map to " + newName;
    }

    public static String getClearLogMessage() {
        return "Event log cleared.";
    }
}
