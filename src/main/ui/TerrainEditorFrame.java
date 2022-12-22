package ui;

import model.*;
import model.Event;
import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Represents a graphical interface to edit Terrain objects
public class TerrainEditorFrame extends JFrame implements ActionListener {

    private static final String NEW_MAP = "NewMap";
    private static final String LOAD_MAP = "LoadMap";
    private static final String SAVE_MAP = "SaveMap";
    private static final String PLACE_PLAIN = "PLACE_PLAIN";
    private static final String PLACE_MOUNTAIN = "PLACE_MOUNTAIN";
    private static final String PLACE_FOREST = "PLACE_FOREST";
    private static final String PLACE_WATER = "PLACE_WATER";
    private static final String PLACE_WALL = "PLACE_WALL";
    private static final String PLACE_CHEST = "PLACE_CHEST";
    private static final String PLACE_GATE = "PLACE_GATE";
    private static final String PLACE_THRONE = "PLACE_THRONE";
    private static final String PLACE_UNIT = "PLACE_UNIT";
    private static final String INSPECT_UNIT = "INSPECT_UNIT";
    private static final String DELETE_UNIT = "DELETE_UNIT";
    private static final String RESIZE_MAP = "ResizeMap";
    private static final String RENAME_MAP = "RenameMap";

    private static final String STARTUP_MAP_NAME = "StartupMap";
    private static final int STARTUP_MAP_WIDTH = 20;
    private static final int STARTUP_MAP_HEIGHT = 15;
    public static final String APP_NAME = "Terrain Editor: ";

    private Terrain currentTerrain;
    private TerrainPanel terrainPanel;
    private JPanel toolPanel;
    EditMode currentEditMode;

    private Faction factionToAdd;
    private BattleClass battleClassToAdd;

    private BufferedImage plainImg;
    private BufferedImage forestImg;
    private BufferedImage mountainImg;
    private BufferedImage waterImg;
    private BufferedImage wallImg;
    private BufferedImage chestImg;
    private BufferedImage gateImg;
    private BufferedImage throneImg;
    private BufferedImage addUnitImg;
    private BufferedImage inspectUnitImg;
    private BufferedImage deleteUnitImg;
    private BufferedImage playerImg;
    private BufferedImage enemyImg;
    private BufferedImage allyImg;

    static final int IMAGE_SIZE = 32;

    private static final FileNameExtensionFilter FILE_FILTER =
            new FileNameExtensionFilter(".json files", ".json");

    // EFFECTS: Constructs a new TerrainBuilderFrame
    public TerrainEditorFrame() {
        super("");
        try {
            loadImages();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize images.");
        }
        initializeVariables();
        initializeLayout();
        initializeMenuBar();
        initializeButtons();
        setCloseOperation();

        pack();
        updateWindowTitle();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    private void updateWindowTitle() {
        setTitle(APP_NAME + currentTerrain.getName());
    }

    // MODIFIES: this
    // EFFECTS: Loads all required images, and throws IOException if there is a problem
    private void loadImages() throws IOException {
        plainImg = ImageIO.read(new File("./res/Plain.png"));
        forestImg = ImageIO.read(new File("./res/Forest.png"));
        mountainImg = ImageIO.read(new File("./res/Mountain.png"));
        waterImg = ImageIO.read(new File("./res/Water.png"));
        wallImg = ImageIO.read(new File("./res/Wall.png"));
        chestImg = ImageIO.read(new File("./res/Chest.png"));
        gateImg = ImageIO.read(new File("./res/Gate.png"));
        throneImg = ImageIO.read(new File("./res/Throne.png"));
        addUnitImg = ImageIO.read(new File("./res/AddSoldierIcon.png"));
        inspectUnitImg = ImageIO.read(new File("./res/ViewSoldierIcon.png"));
        deleteUnitImg = ImageIO.read(new File("./res/DeleteSoldierIcon.png"));
        playerImg = ImageIO.read(new File("./res/PlayerSoldierIcon.png"));
        enemyImg = ImageIO.read(new File("./res/EnemySoldierIcon.png"));
        allyImg = ImageIO.read(new File("./res/AllySoldierIcon.png"));
    }

    // MODIFIES: this
    // EFFECTS: Initializes all the variables for the TerrainEditorFrame
    private void initializeVariables() {
        currentEditMode = EditMode.PLACE_PLAIN;
        currentTerrain = new Terrain(STARTUP_MAP_NAME, STARTUP_MAP_WIDTH, STARTUP_MAP_HEIGHT);
        factionToAdd = Faction.PLAYER;
        battleClassToAdd = BattleClass.LORD;
    }

    // MODIFIES: this
    // EFFECTS: Handles user actions
    @SuppressWarnings("methodlength")
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case SAVE_MAP:
                handleSaveMap();
                break;
            case LOAD_MAP:
                handleLoadMap();
                break;
            case RESIZE_MAP:
                handleResizeMap();
                break;
            case RENAME_MAP:
                handleRenameMap();
                break;
            case NEW_MAP:
                handleNewMap();
                break;
            case PLACE_UNIT:
                handlePlaceUnitButton();
                break;
            default:
                if (EditMode.isMode(e.getActionCommand())) {
                    currentEditMode = EditMode.valueOf(e.getActionCommand());
                }
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: Initializes the menu bar
    private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = initializeFileMenu();
        JMenu editMenu = initializeEditMenu();

        this.setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
    }

    // EFFECTS: Returns the initialized editMenu
    private JMenu initializeEditMenu() {
        JMenuItem menuItem;
        JMenu editMenu = new JMenu("Edit");
        menuItem = new JMenuItem("Rename");
        menuItem.setActionCommand(RENAME_MAP);
        menuItem.addActionListener(this);
        editMenu.add(menuItem);
        menuItem = new JMenuItem("Resize");
        menuItem.setActionCommand(RESIZE_MAP);
        menuItem.addActionListener(this);
        editMenu.add(menuItem);
        return editMenu;
    }

    // EFFECTS: Returns the initialized fileMenu
    private JMenu initializeFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem menuItem = new JMenuItem("New");
        menuItem.setActionCommand(NEW_MAP);
        menuItem.addActionListener(this);
        fileMenu.add(menuItem);
        menuItem = new JMenuItem("Load");
        menuItem.setActionCommand(LOAD_MAP);
        menuItem.addActionListener(this);
        fileMenu.add(menuItem);
        menuItem = new JMenuItem("Save");
        menuItem.setActionCommand(SAVE_MAP);
        menuItem.addActionListener(this);
        fileMenu.add(menuItem);
        return fileMenu;
    }

    // MODIFIES: this
    // EFFECTS: Initializes the main layout
    private void initializeLayout() {
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        toolPanel = new JPanel(new FlowLayout());
        terrainPanel = new TerrainPanel(this);
        this.add(terrainPanel);
        this.add(toolPanel);
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
    }

    // MODIFIES: this
    // EFFECTS: Sets up the buttons
    private void initializeButtons() {
        setUpButton(plainImg, PLACE_PLAIN);
        setUpButton(mountainImg, PLACE_MOUNTAIN);
        setUpButton(forestImg, PLACE_FOREST);
        setUpButton(waterImg, PLACE_WATER);
        setUpButton(wallImg, PLACE_WALL);
        setUpButton(chestImg, PLACE_CHEST);
        setUpButton(gateImg, PLACE_GATE);
        setUpButton(throneImg, PLACE_THRONE);
        setUpButton(addUnitImg, PLACE_UNIT);
        setUpButton(inspectUnitImg, INSPECT_UNIT);
        setUpButton(deleteUnitImg, DELETE_UNIT);
    }

    // MODIFIES: this
    // EFFECTS: Sets the close operation for the TerrainEditorFrame
    // Modeled after method from https://www.clear.rice.edu/comp310/JavaResources/frame_close.html
    private void setCloseOperation() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                printLog();
                System.exit(0);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: Sets up a button with the given icon and action command
    private void setUpButton(BufferedImage icon, String actionCommand) {
        JButton button = new JButton(new ImageIcon(icon));
        button.setActionCommand(actionCommand);
        button.addActionListener(this);
        toolPanel.add(button);
    }

    // MODIFIES: this
    // EFFECTS: Handles creating a new map
    private void handleNewMap() {
        initializeVariables();
        terrainPanel.setSize(terrainPanel.getPreferredSize());
        pack();
    }

    // MODIFIES: this
    // EFFECTS: Handles renaming the map
    private void handleRenameMap() {
        String newName = JOptionPane.showInputDialog(this,
                "Enter the map's new name:",
                "Rename Map", JOptionPane.PLAIN_MESSAGE);
        if (newName != null && newName.equals("")) {
            JOptionPane.showMessageDialog(this, "Invalid name",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } else if (newName != null) {
            currentTerrain.rename(newName);
            this.setTitle(newName);
        }
    }

    // MODIFIES: this
    // EFFECTS: Handles resizing the map
    // Following code modeled after https://stackoverflow.com/questions/11211286/
    private void handleResizeMap() {
        TerrainResizeDialog terrainResizeDialog = new TerrainResizeDialog(this);
        terrainResizeDialog.setVisible(true);
        if (!terrainResizeDialog.isCancelled()) {
            try {
                int newWidth = terrainResizeDialog.getNewWidth();
                int newHeight = terrainResizeDialog.getNewHeight();
                if (newWidth < STARTUP_MAP_HEIGHT || newHeight < 10) {
                    JOptionPane.showMessageDialog(this,
                            "Width must be at least 15 and height must be at least 10",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    currentTerrain.resize(newWidth, newHeight);
                    terrainPanel.setSize(terrainPanel.getPreferredSize());
                    pack();
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Please enter an integer for both width and height.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // EFFECTS: Saves the terrain to json based on user input
    // Code modeled after https://stackoverflow.com/questions/17010647/set-default-saving-extension-with-jfilechooser
    // and https://stackoverflow.com/questions/3571223/how-do-i-get-the-file-extension-of-a-file-in-java
    private void handleSaveMap() {
        JFileChooser fc = new JFileChooser("./data/mapSaves");
        fc.setSelectedFile(new File(currentTerrain.getName() + ".json"));
        int response = fc.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            String fileName = f.getName();
            String path = f.getPath();
            String extension = "";
            int i = fileName.lastIndexOf('.');
            if (i > 0) {
                extension = fileName.substring(i + 1);
            }
            if (i == -1 || !extension.equalsIgnoreCase("json")) {
                path += ".json";
            }
            writeToJson(path);
        }
    }

    // EFFECTS: Writes the current terrain to json at the given path
    private void writeToJson(String path) {
        JsonWriter jw = new JsonWriter(path);
        try {
            jw.open();
            jw.write(currentTerrain);
            jw.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // MODIFIES: this
    // EFFECTS: Loads a new map based on user input
    // Code modeled after method from https://www.geeksforgeeks.org/java-swing-jfilechooser/
    private void handleLoadMap() {
        JFileChooser fc = new JFileChooser("./data/mapSaves");
        fc.addChoosableFileFilter(FILE_FILTER);
        int response = fc.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getPath();
            JsonReader reader = new JsonReader(path);
            try {
                currentTerrain = reader.readTerrain();
                updateWindowTitle();
            } catch (IOException | JSONException e) {
                JOptionPane.showMessageDialog(this, "Failed to read map", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        terrainPanel.setSize(terrainPanel.getPreferredSize());
        repaint();
        pack();
    }

    // MODIFIES: this
    // EFFECTS: Inspects and modifies units based on user input
    // Code modeled after https://stackoverflow.com/questions/7855227/wait-for-jdialog-to-close
    void handleInspectUnit(int mapX, int mapY) {
        Unit unitToEdit = currentTerrain.getUnit(mapX, mapY);
        if (unitToEdit != null) {
            UnitEditorDialog unitEditorDialog =
                    new UnitEditorDialog(this, unitToEdit.getFaction(), unitToEdit.getBattleClass());
            unitEditorDialog.setVisible(true);
            if (!unitEditorDialog.isCancelled()) {
                unitToEdit.setFaction(unitEditorDialog.getFaction());
                unitToEdit.setBattleClass(unitEditorDialog.getBattleClass());
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Handles the click of the place unit button
    private void handlePlaceUnitButton() {
        UnitEditorDialog unitEditorDialog = new UnitEditorDialog(this, factionToAdd, battleClassToAdd);
        unitEditorDialog.setVisible(true);
        if (!unitEditorDialog.isCancelled()) {
            factionToAdd = unitEditorDialog.getFaction();
            battleClassToAdd = unitEditorDialog.getBattleClass();
            this.currentEditMode = EditMode.PLACE_UNIT;
        }
    }

    // MODIFIES: this
    // EFFECTS: Places unit on the terrain based on user input
    void handlePlaceUnit(int mapX, int mapY) {
        currentTerrain.addUnit(new Unit(factionToAdd, battleClassToAdd, mapX, mapY));
    }

    // MODIFIES: this
    // EFFECTS: Deletes unit based on user input
    void handleDeleteUnit(int mapX, int mapY) {
        currentTerrain.deleteUnit(mapX, mapY);
    }

    // MODIFIES: this
    // EFFECTS: Handles changes to the Terrain based on clicks occurring on the map
    @SuppressWarnings("methodlength")
    void handleClick(int mapX, int mapY, boolean dragged) {
        switch (currentEditMode) {
            case PLACE_PLAIN:
                currentTerrain.setTile(TerrainTile.PLAIN, mapX, mapY);
                break;
            case PLACE_MOUNTAIN:
                currentTerrain.setTile(TerrainTile.MOUNTAIN, mapX, mapY);
                break;
            case PLACE_FOREST:
                currentTerrain.setTile(TerrainTile.FOREST, mapX, mapY);
                break;
            case PLACE_WATER:
                currentTerrain.setTile(TerrainTile.WATER, mapX, mapY);
                break;
            case PLACE_WALL:
                currentTerrain.setTile(TerrainTile.WALL, mapX, mapY);
                break;
            case PLACE_CHEST:
                currentTerrain.setTile(TerrainTile.CHEST, mapX, mapY);
                break;
            case PLACE_GATE:
                currentTerrain.setTile(TerrainTile.GATE, mapX, mapY);
                break;
            case PLACE_THRONE:
                currentTerrain.setTile(TerrainTile.THRONE, mapX, mapY);
                break;
            case DELETE_UNIT:
                handleDeleteUnit(mapX, mapY);
                break;
            case PLACE_UNIT:
                if (!dragged) {
                    handlePlaceUnit(mapX, mapY);
                }
                break;
            default:
                if (!dragged) {
                    handleInspectUnit(mapX, mapY);
                }
                break;
        }
    }

    // EFFECTS: Returns the current terrain
    Terrain getCurrentTerrain() {
        return currentTerrain;
    }

    // EFFECTS: Returns the image of the given faction
    BufferedImage getImageOfUnit(Faction faction) {
        switch (faction) {
            case PLAYER:
                return playerImg;
            case ENEMY:
                return enemyImg;
            default:
                return allyImg;
        }
    }

    // EFFECTS: Returns the image corresponding to the terrain tile type.
    BufferedImage getImageOfTerrain(TerrainTile tileType) {
        switch (tileType) {
            case PLAIN:
                return plainImg;
            case MOUNTAIN:
                return mountainImg;
            case FOREST:
                return forestImg;
            case WATER:
                return waterImg;
            case WALL:
                return wallImg;
            case CHEST:
                return chestImg;
            case GATE:
                return gateImg;
            default:
                return throneImg;
        }
    }

    public static void printLog() {
        EventLog el = EventLog.getInstance();
        for (Event event : el) {
            System.out.println(event.toString());
        }
    }

}