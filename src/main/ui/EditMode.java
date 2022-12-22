package ui;

// An enumeration representing an edit mode for the TerrainBuilderFrame
public enum EditMode {
    PLACE_PLAIN,
    PLACE_MOUNTAIN,
    PLACE_FOREST,
    PLACE_WATER,
    PLACE_WALL,
    PLACE_CHEST,
    PLACE_GATE,
    PLACE_THRONE,
    PLACE_UNIT,
    INSPECT_UNIT,
    DELETE_UNIT;


    // EFFECTS: Returns true if the given string is a mode, returns false otherwise.
    public static boolean isMode(String s) {
        try {
            EditMode.valueOf(s);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

