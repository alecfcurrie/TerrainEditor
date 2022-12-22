package ui;

import ui.exceptions.InitializationFailedException;
import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Represents the text-based Terrain creation application. Handles all user input, and allows them to create, edit, and
// delete Terrains, as per the user stories in the readme file.
// Modeled after TellerAppNotRobust EdX project
public class TerrainEditorConsoleApp {

    private static final String QUIT_COMMAND = "quit";
    private static final String NEW_COMMAND = "n";
    private static final String EDIT_COMMAND = "e";
    private static final String DELETE_COMMAND = "d";

    private static final String EXIT_EDITING_COMMAND = "exit";
    private static final String LIST_COMMAND = "list";
    private static final String RENAME_COMMAND = "n";
    private static final String RESIZE_COMMAND = "r";
    private static final String SET_TERRAIN_COMMAND = "s";
    private static final String GET_TERRAIN_COMMAND = "g";
    private static final String ADD_UNIT_COMMAND = "a";
    private static final String VIEW_UNIT_COMMAND = "v";
    private static final String EDIT_UNIT_COMMAND = "e";
    private static final String MOVE_UNIT_COMMAND = "m";
    private static final String DELETE_UNIT_COMMAND = "d";

    private static final String LORD_CODE = "lord";
    private static final String SOLDIER_CODE = "soldier";
    private static final String FIGHTER_CODE = "fighter";
    private static final String MYRMIDON_CODE = "myrm";
    private static final String ARCHER_CODE = "archer";
    private static final String HEALER_CODE = "healer";
    private static final String NINJA_CODE = "ninja";
    private static final String THIEF_CODE = "thief";
    private static final String CAVALIER_CODE = "cav";
    private static final String PEG_KNIGHT_CODE = "pegasus";
    private static final String WYVERN_RIDER_CODE = "wyvern";
    private static final String MAGE_CODE = "mage";

    private static final String PLAYER_CODE = "p";
    private static final String ENEMY_CODE = "e";
    private static final String ALLY_CODE = "a";

    private static final String PLAIN_CODE = "plain";
    private static final String MOUNTAIN_CODE = "mountain";
    private static final String WATER_CODE = "water";
    private static final String FOREST_CODE = "forest";
    private static final String WALL_CODE = "wall";
    private static final String CHEST_CODE = "chest";
    private static final String GATE_CODE = "gate";
    private static final String THRONE_CODE = "throne";


    private Terrain currentTerrain;


    private static final String JSON_STORE = "./data/TerrainExample.json";
    private TerrainList terrainList;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    //MODIFIES: this
    //EFFECTS: Runs the terrain editor console-based application
    public TerrainEditorConsoleApp() {
        input = new Scanner(System.in);
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        try {
            loadTerrainList();
            runApp();
        } catch (InitializationFailedException e) {
            System.err.println("Initialization failed. Quitting application.");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads terrain list from memory
    private void loadTerrainList() throws InitializationFailedException {
        try {
            terrainList = jsonReader.readTerrainList();
            System.out.println("Loaded terrain list from " + JSON_STORE);
        } catch (Exception e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
            System.out.println(e.getMessage());
            System.out.println("Create new terrain list? y/n");
            boolean answerIsYes = getYesOrNo();
            if (answerIsYes) {
                terrainList = new TerrainList();
            } else {
                throw new InitializationFailedException();
            }
        }
    }

    // EFFECTS: Returns true if user enters y, or false if user enters n
    private boolean getYesOrNo() {
        while (true) {
            String command = input.nextLine().toLowerCase();
            if (command.equals("y")) {
                return true;
            } else if (command.equals("n")) {
                return false;
            } else {
                System.err.println("Invalid input. Please try again.");
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: Displays the main menu and allows user to manage their terrain collection
    private void runApp() {
        while (true) {
            displayMainMenu();
            String command = input.nextLine().toLowerCase();

            if (command.equals(QUIT_COMMAND)) {
                System.out.println("Would you like to save your terrain list? y/n");
                boolean answerIsYes = getYesOrNo();
                if (answerIsYes) {
                    saveTerrainList();
                }
                System.out.println("\nQuitting the application.");
                break;
            } else {
                handleMainMenuCommand(command);
            }
        }
    }

    //EFFECTS: Displays main menu options
    private void displayMainMenu() {
        System.out.println("\n------TERRAIN MAKER------");
        System.out.println("------Main Menu------");
        System.out.println("\t-" + NEW_COMMAND + " -> Create and edit a new terrain. "
                + "All tiles are initialized as plain tiles");
        System.out.println("\t-" + EDIT_COMMAND + " -> Edit an existing terrain");
        System.out.println("\t-" + DELETE_COMMAND + " -> Delete an existing terrain");
        System.out.println("\t-" + QUIT_COMMAND + " -> Optionally saves all changes to the terrain list, "
                + "then quits the application");
        System.out.println("---------------------");
    }

    //MODIFIES: this
    //EFFECTS: Processes main menu commands
    private void handleMainMenuCommand(String command) {
        switch (command) {
            case NEW_COMMAND:
                handleNewCommand();
                break;
            case EDIT_COMMAND:
                handleEditCommand();
                break;
            case DELETE_COMMAND:
                handleDeleteCommand();
                break;
            default:
                System.out.println("Invalid command.");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: Deletes a terrain from the terrain list based on user input.
    private void handleDeleteCommand() {
        if (terrainList.size() == 0) {
            System.out.println("The terrain list empty. This action cannot be performed.");
        } else {
            System.out.println("Enter the name of the terrain you would like to delete:");
            String name = input.nextLine();
            boolean foundTerrain = false;
            for (Terrain terrain : terrainList) {
                if (terrain.getName().equals(name)) {
                    terrainList.remove(terrain);
                    System.out.println(name + " deleted.");
                    foundTerrain = true;
                    break;
                }
            }
            if (!foundTerrain) {
                System.out.println("No such terrain exists.");
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: creates a new terrain, and adds it to the terrain list
    private void handleNewCommand() {
        Terrain terrain;
        String name = createName("new terrain's");
        int width = createTerrainNumber("new terrain's width", Terrain.MIN_WIDTH);
        int height = createTerrainNumber("new terrain's height", Terrain.MIN_HEIGHT);
        terrain = new Terrain(name, width, height);
        terrainList.add(terrain);
        editTerrain(terrain);
    }

    //EFFECTS: Returns a string for a terrain name, making sure the new terrain name is not already taken
    private String createName(String terrainDetails) {
        String name;

        while (true) {
            boolean nameTaken = false;
            System.out.println("Enter the " + terrainDetails + " name:");
            name = input.nextLine();
            for (Terrain terrain : terrainList) {
                if (terrain.getName().equals(name)) {
                    nameTaken = true;
                    break;
                }
            }
            if (!nameTaken) {
                return name;
            } else {
                System.out.println("This name is already taken. Please try again.");
            }
        }
    }

    //EFFECTS: returns an integer that is at least the minimum value.
    private int createTerrainNumber(String parameter, int minimumValue) {
        int result;

        while (true) {
            System.out.println("Enter the " + parameter + ":");
            result = input.nextInt();
            input.nextLine();
            if (result >= minimumValue) {
                return result;
            } else {
                System.out.println("The " + parameter + " must be at least " + minimumValue + ". Please try again.");
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: Initializes the terrain editing process from the main menu.
    private void handleEditCommand() {
        if (terrainList.size() == 0) {
            System.out.println("The terrain list empty. Please create a terrain first.");
        } else {
            System.out.println("Enter the name of the terrain you would like to edit:");
            String name = input.nextLine();
            boolean foundTerrain = false;
            for (Terrain terrain : terrainList) {
                if (terrain.getName().equals(name)) {
                    editTerrain(terrain);
                    foundTerrain = true;
                    break;
                }
            }
            if (!foundTerrain) {
                System.out.println("No such terrain exists.");
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: Allows the user to edit a terrain.
    private void editTerrain(Terrain terrain) {
        currentTerrain = terrain;
        System.out.println("Editing " + terrain.getName());
        System.out.println("Note that terrain coordinates are zero-indexed, where the origin is the top left tile.");
        while (true) {
            System.out.println("Type \"" + LIST_COMMAND + "\" for a command list, or begin typing commands.");
            String command = input.nextLine().toLowerCase();

            if (command.equals(EXIT_EDITING_COMMAND)) {
                System.out.println("\nExiting edit mode.");
                break;
            } else {
                handleEditMenuCommand(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Handles all terrain edit commands except for quitting.
    @SuppressWarnings("methodlength")
    private void handleEditMenuCommand(String command) {
        switch (command) {
            case LIST_COMMAND:
                listTerrainEditCommands();
                break;
            case RENAME_COMMAND:
                handleRenameTerrain();
                break;
            case RESIZE_COMMAND:
                handleResizeTerrain();
                break;
            case SET_TERRAIN_COMMAND:
                handleSetTerrain();
                break;
            case GET_TERRAIN_COMMAND:
                handleGetTerrain();
                break;
            case ADD_UNIT_COMMAND:
                handleAddUnit();
                break;
            case VIEW_UNIT_COMMAND:
                handleViewUnit();
                break;
            case EDIT_UNIT_COMMAND:
                handleEditUnit();
                break;
            case MOVE_UNIT_COMMAND:
                handleMoveUnit();
                break;
            case DELETE_UNIT_COMMAND:
                handleDeleteUnit();
                break;
            default:
                System.out.println("Invalid input. Please try again.");
                break;
        }
    }

    //MODIFIES: this
    //EFFECTS: Lists all terrain commands.
    private void listTerrainEditCommands() {
        System.out.println("\"" + LIST_COMMAND + "\" -> Lists all terrain editing commands.");
        System.out.println("\"" + RENAME_COMMAND + "\" -> Renames the terrain.");
        System.out.println("\"" + RESIZE_COMMAND + "\" -> Resizes the given terrain.");
        System.out.println("\"" + SET_TERRAIN_COMMAND + "\" -> Sets a given tile's terrain.");
        System.out.println("\"" + GET_TERRAIN_COMMAND + "\" -> Gets a given tile's terrain.");
        System.out.println("\"" + ADD_UNIT_COMMAND + "\" -> Creates a unit and adds them to the terrain.");
        System.out.println("\"" + VIEW_UNIT_COMMAND + "\" -> Views a unit's class and faction");
        System.out.println("\"" + EDIT_UNIT_COMMAND + "\" -> Edits a unit's battle class and faction.");
        System.out.println("\"" + MOVE_UNIT_COMMAND + "\" -> Moves a unit to a different location.");
        System.out.println("\"" + DELETE_UNIT_COMMAND + "\" -> Deletes the unit at the given position.");
        System.out.println("\"" + EXIT_EDITING_COMMAND + "\" -> Exit terrain edit mode and return to the main menu.");
        System.out.println("--------------------");
    }

    //MODIFIES: this
    //EFFECTS: Renames the current terrain based on user input.
    private void handleRenameTerrain() {
        currentTerrain.rename(createName("terrain's new"));
        System.out.println("Terrain renamed successfully to " + currentTerrain.getName() + ".");
    }

    // MODIFIES: this
    // EFFECTS: Sets the terrain at coordinates given by user input
    private void handleSetTerrain() {
        Point point = getCoordinates("terrain's");
        TerrainTile terrainTile = getNewTerrain();
        currentTerrain.setTile(terrainTile, point.x, point.y);
        System.out.println("Terrain set complete.");
    }

    // EFFECTS: Displays the terrain at the coordinates given by user input
    private void handleGetTerrain() {
        Point point = getCoordinates("terrain's");
        System.out.println("The terrain at (" + point.x + ", " + point.y + ") is "
                + currentTerrain.getTileType(point.x, point.y).name().toLowerCase() + ".");
    }

    // MODIFIES: this
    // EFFECTS: Adds a unit based on user input
    private void handleAddUnit() {
        Faction faction = getNewFaction();
        BattleClass battleClass = getNewBattleClass();
        Point point = getCoordinates("new unit's");
        Unit unit = new Unit(faction, battleClass, point.x, point.y);
        if (!currentTerrain.addUnit(unit)) {
            System.out.println("Invalid unit placement. Please try again.");
        } else {
            System.out.println("Unit added successfully.");
        }
    }

    // EFFECTS: Displays the class and faction of a unit at the coordinates given by user input
    private void handleViewUnit() {
        Unit unit = getUnitOnTerrain("unit's");
        if (unit == null) {
            System.out.println("No unit on the given tile.");
        } else {
            System.out.println("The unit's faction is " + unit.getFaction().name().toLowerCase() + ".");
            System.out.println("The unit's battle class is " + unit.getBattleClass().name()
                    .toLowerCase().replace("_", " ") + ".");
        }
    }

    // EFFECTS: returns a unit at the coordinates given by user input
    private Unit getUnitOnTerrain(String details) {
        Point point = getCoordinates(details);
        return currentTerrain.getUnit(point.x, point.y);
    }

    //MODIFIES: this
    //EFFECTS: edits a unit's faction and battle class based on user input
    private void handleEditUnit() {
        Unit toEdit = getUnitOnTerrain("unit's");
        if (toEdit == null) {
            System.out.println("No unit on the given tile.");
        } else {
            toEdit.setFaction(getNewFaction());
            toEdit.setBattleClass(getNewBattleClass());
            System.out.println("Unit editing complete.");
        }
    }

    // MODIFIES: this
    // EFFECTS: moves a unit based on user input
    private void handleMoveUnit() {
        Unit toMove = getUnitOnTerrain("unit's current");
        if (toMove == null) {
            System.out.println("No unit on the given tile.");
        } else {
            Point point = getCoordinates("unit's new");
            if (currentTerrain.moveUnit(toMove, point.x, point.y)) {
                System.out.println("Unit moved successfully.");
            } else {
                System.out.println("New tile is occupied. Please try again.");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: deletes a unit based on user input
    private void handleDeleteUnit() {
        Point point = getCoordinates("unit's");
        boolean successful = currentTerrain.deleteUnit(point.x, point.y);
        if (successful) {
            System.out.println("Unit deleted.");
        } else {
            System.out.println("Tile was unoccupied. No unit deleted.");
        }
    }

    // MODIFIES: this
    // EFFECTS: resizes the Terrain based on user input
    private void handleResizeTerrain() {
        int width = createTerrainNumber("terrain's new width", Terrain.MIN_WIDTH);
        int height = createTerrainNumber("terrain's new height", Terrain.MIN_HEIGHT);
        currentTerrain.resize(width, height);
    }

    // EFFECTS: Returns a point based on user input
    private Point getCoordinates(String object) {
        int x = createCoordinate(object, "x", currentTerrain.getWidth());
        int y = createCoordinate(object, "y", currentTerrain.getHeight());
        return new Point(x, y);
    }

    // EFFECTS: Returns an integer representing a coordinate based on user input
    private int createCoordinate(String object, String coordinate, int maximumValue) {
        int result;

        while (true) {
            System.out.println("Enter the " + object + " " + coordinate + " position:");
            result = input.nextInt();
            input.nextLine();
            //Scanner issue solved with
            //https://stackoverflow.com/questions/13102045/scanner-is-skipping-nextline-after-using-next-or-nextfoo
            if (result < 0) {
                System.out.println("The coordinate must be at least 0. Please try again.");
            } else if (result >= maximumValue) {
                System.out.println("The coordinate must be less than " + maximumValue + ". Please try again.");
            } else {
                return result;
            }
        }
    }

    //EFFECTS: Returns a Faction based on user input
    private Faction getNewFaction() {
        System.out.println("Set the unit's faction.");
        while (true) {
            System.out.println("Type \"" + PLAYER_CODE + "\" for player, "
                    + "\"" + ENEMY_CODE + "\" for enemy, and "
                    + "\"" + ALLY_CODE + "\" for ally.");
            String factionStr = input.nextLine().toLowerCase();
            switch (factionStr) {
                case PLAYER_CODE:
                    return Faction.PLAYER;
                case ENEMY_CODE:
                    return Faction.ENEMY;
                case ALLY_CODE:
                    return Faction.ALLY;
                default:
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }
    }


    //EFFECTS: Returns a BattleClass based on user input
    @SuppressWarnings("methodlength")
    private BattleClass getNewBattleClass() {
        System.out.println("Set the unit's battle class.");
        while (true) {
            System.out.println("Type \"" + LIST_COMMAND + "\" to see what to type to set the class,"
                    + " or type the class code if you know it already.");
            String classStr = input.nextLine().toLowerCase();
            switch (classStr) {
                case LIST_COMMAND:
                    listBattleClasses();
                    break;
                case LORD_CODE:
                    return BattleClass.LORD;
                case SOLDIER_CODE:
                    return BattleClass.SOLDIER;
                case FIGHTER_CODE:
                    return BattleClass.FIGHTER;
                case MYRMIDON_CODE:
                    return BattleClass.MYRMIDON;
                case ARCHER_CODE:
                    return BattleClass.ARCHER;
                case HEALER_CODE:
                    return BattleClass.HEALER;
                case NINJA_CODE:
                    return BattleClass.NINJA;
                case THIEF_CODE:
                    return BattleClass.THIEF;
                case CAVALIER_CODE:
                    return BattleClass.CAVALIER;
                case PEG_KNIGHT_CODE:
                    return BattleClass.PEGASUS_KNIGHT;
                case WYVERN_RIDER_CODE:
                    return BattleClass.WYVERN_RIDER;
                case MAGE_CODE:
                    return BattleClass.MAGE;
                default:
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }
    }

    //EFFECTS: Lists all codes for setting unit battle class
    private void listBattleClasses() {
        System.out.println("\"" + LORD_CODE + "\" -> Lord class");
        System.out.println("\"" + SOLDIER_CODE + "\" -> Soldier class");
        System.out.println("\"" + FIGHTER_CODE + "\" -> Fighter class");
        System.out.println("\"" + MYRMIDON_CODE + "\" -> Myrmidon/Swordmaster class");
        System.out.println("\"" + ARCHER_CODE + "\" -> Archer class");
        System.out.println("\"" + HEALER_CODE + "\" -> Healer class");
        System.out.println("\"" + NINJA_CODE + "\" -> Ninja class");
        System.out.println("\"" + THIEF_CODE + "\" -> Thief class");
        System.out.println("\"" + CAVALIER_CODE + "\" -> Cavalier class");
        System.out.println("\"" + PEG_KNIGHT_CODE + "\" -> Pegasus knight class");
        System.out.println("\"" + WYVERN_RIDER_CODE + "\" -> Wyvern knight class");
        System.out.println("\"" + MAGE_CODE + "\" -> Mage class");
        System.out.println("---------------------");
    }

    @SuppressWarnings("methodlength")
    //EFFECTS: Returns a terrain type based on user's input
    private TerrainTile getNewTerrain() {
        System.out.println("Change the terrain to a new type.");
        while (true) {
            System.out.println("Type \"" + LIST_COMMAND + "\" to see what to type to set the terrain,"
                    + " or type the terrain code if you know it already");
            String terrainStr = input.nextLine().toLowerCase();
            switch (terrainStr) {
                case LIST_COMMAND:
                    listTerrain();
                    break;
                case PLAIN_CODE:
                    return TerrainTile.PLAIN;
                case MOUNTAIN_CODE:
                    return TerrainTile.MOUNTAIN;
                case WATER_CODE:
                    return TerrainTile.WATER;
                case FOREST_CODE:
                    return TerrainTile.FOREST;
                case WALL_CODE:
                    return TerrainTile.WALL;
                case CHEST_CODE:
                    return TerrainTile.CHEST;
                case GATE_CODE:
                    return TerrainTile.GATE;
                case THRONE_CODE:
                    return TerrainTile.THRONE;
                default:
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }
    }

    // EFFECTS: List all codes for setting terrain
    private void listTerrain() {
        System.out.println("\"" + PLAIN_CODE + "\" -> Plain terrain");
        System.out.println("\"" + MOUNTAIN_CODE + "\" -> Mountainous terrain");
        System.out.println("\"" + FOREST_CODE + "\" -> Forested terrain");
        System.out.println("\"" + WATER_CODE + "\" -> River/Lake/Sea terrain");
        System.out.println("\"" + WALL_CODE + "\" -> Wall");
        System.out.println("\"" + CHEST_CODE + "\" -> Treasure chest");
        System.out.println("\"" + GATE_CODE + "\" -> Castle gate");
        System.out.println("\"" + THRONE_CODE + "\" -> Castle throne");
        System.out.println("---------------------");
    }

    // Methods from this point onwards modeled after JsonSerializationDemo project
    // EFFECTS: saves the workroom to file
    private void saveTerrainList() {
        try {
            jsonWriter.open();
            jsonWriter.write(terrainList);
            jsonWriter.close();
            System.out.println("Automatically saved terrain list to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

}