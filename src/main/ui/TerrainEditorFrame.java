package ui;

import model.*;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Represents a graphical interface to edit Terrain objects
 */
public class TerrainEditorFrame extends JFrame implements ActionListener {

//    private static final Logger log = Logger.getLogger(TerrainEditorFrame.class.getName());
    // Action commands
    private static final String NEW_MAP_AC = "NewMap";
    private static final String LOAD_MAP_AC = "LoadMap";
    private static final String SAVE_MAP_AC = "SaveMap";
    private static final String PLACE_PLAIN_AC = "PLACE_PLAIN";
    private static final String PLACE_MOUNTAIN_AC = "PLACE_MOUNTAIN";
    private static final String PLACE_FOREST_AC = "PLACE_FOREST";
    private static final String PLACE_WATER_AC = "PLACE_WATER";
    private static final String PLACE_WALL_AC = "PLACE_WALL";
    private static final String PLACE_CHEST_AC = "PLACE_CHEST";
    private static final String PLACE_GATE_AC = "PLACE_GATE";
    private static final String PLACE_THRONE_AC = "PLACE_THRONE";
    private static final String PLACE_UNIT_AC = "PLACE_UNIT";
    private static final String INSPECT_UNIT_AC = "INSPECT_UNIT";
    private static final String DELETE_UNIT_AC = "DELETE_UNIT";
    private static final String RESIZE_MAP_AC = "ResizeMap";
    private static final String RENAME_MAP_AC = "RenameMap";

    private static final String STARTUP_MAP_NAME = "StartupMap";
    private static final int STARTUP_MAP_WIDTH = 20;
    private static final int STARTUP_MAP_HEIGHT = 15;
    public static final String APP_NAME = "Terrain Editor: ";
    private static final String EXIT_AC = "EXIT";

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

    /**
     * Constructs a new TerrainBuilderFrame.
     */
    public TerrainEditorFrame() {
        super("");
        try {
            loadImages();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize images.");
        }
        // Set look and feel to match the platform.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
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

    /**
     * Updates the window title to be app name + name of current Terrain
     */
    private void updateWindowTitle() {
        setTitle(APP_NAME + currentTerrain.getName());
    }

    /**
     * Loads all required images
     *
     * @throws IOException if there is an issue loading images
     */
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

    /**
     * Initializes all the variables for the TerrainEditorFrame
     */
    private void initializeVariables() {
        currentEditMode = EditMode.PLACE_PLAIN;
        currentTerrain = new Terrain(STARTUP_MAP_NAME, STARTUP_MAP_WIDTH, STARTUP_MAP_HEIGHT);
        factionToAdd = Faction.PLAYER;
        battleClassToAdd = BattleClass.LORD;
    }

    /**
     * Handles user actions
     *
     * @param e the event to be processed
     */
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case SAVE_MAP_AC:
                handleSaveMap();
                break;
            case LOAD_MAP_AC:
                handleLoadMap();
                break;
            case RESIZE_MAP_AC:
                handleResizeMap();
                break;
            case RENAME_MAP_AC:
                handleRenameMap();
                break;
            case NEW_MAP_AC:
                handleNewMap();
                break;
            case PLACE_UNIT_AC:
                handlePlaceUnitButton();
                break;
            case EXIT_AC:
                // from https://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            default:
                if (EditMode.isMode(e.getActionCommand())) {
                    currentEditMode = EditMode.valueOf(e.getActionCommand());
                }
                break;
        }
    }

    /**
     * Initializes the menu bar
     */
    private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = initializeFileMenu();
        JMenu editMenu = initializeEditMenu();

        this.setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
    }

    /**
     * Initializes the Edit menu
     *
     * @return the initialized Edit menu
     */
    private JMenu initializeEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        addMenuItem(editMenu, "Rename", RENAME_MAP_AC);
        addMenuItem(editMenu, "Resize", RESIZE_MAP_AC);
        return editMenu;
    }

    /**
     * Initializes the File menu
     *
     * @return the initialized File menu
     */
    private JMenu initializeFileMenu() {
        JMenu fileMenu = new JMenu("File");

        addMenuItem(fileMenu, "New", NEW_MAP_AC);
        addMenuItem(fileMenu, "Load", LOAD_MAP_AC);
        addMenuItem(fileMenu, "Save", SAVE_MAP_AC);
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Exit", EXIT_AC);
        return fileMenu;
    }

    /**
     * Adds a menu item to the given menu
     *
     * @param menu The {@link JMenu} to add the {@link JMenuItem} to
     * @param menuText The text of the {@link JMenuItem}
     * @param actionCommand The associated action command of the {@link JMenuItem}
     */
    private void addMenuItem(JMenu menu, String menuText, String actionCommand) {
        JMenuItem menuItem = new JMenuItem(menuText);
        menuItem.setActionCommand(actionCommand);
        menuItem.addActionListener(this);
        menuItem.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        menu.add(menuItem);
    }

    /**
     * Initializes the main layout.
     */
    private void initializeLayout() {
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        toolPanel = new JPanel(new FlowLayout());
        terrainPanel = new TerrainPanel(this);
        this.add(terrainPanel);
        this.add(toolPanel);
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
    }

    /**
     * Sets up all the buttons
     */
    private void initializeButtons() {
        setUpButton(plainImg, PLACE_PLAIN_AC);
        setUpButton(mountainImg, PLACE_MOUNTAIN_AC);
        setUpButton(forestImg, PLACE_FOREST_AC);
        setUpButton(waterImg, PLACE_WATER_AC);
        setUpButton(wallImg, PLACE_WALL_AC);
        setUpButton(chestImg, PLACE_CHEST_AC);
        setUpButton(gateImg, PLACE_GATE_AC);
        setUpButton(throneImg, PLACE_THRONE_AC);
        setUpButton(addUnitImg, PLACE_UNIT_AC);
        setUpButton(inspectUnitImg, INSPECT_UNIT_AC);
        setUpButton(deleteUnitImg, DELETE_UNIT_AC);
    }

    /**
     * Sets the close operation for the TerrainEditorFrame
     * Modeled after method from <a href="https://www.clear.rice.edu/comp310/JavaResources/frame_close.html">...</a>
     */
    private void setCloseOperation() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                Logger.getLogger(TerrainEditorFrame.class.getName()).info("Closing application.");
                System.exit(0);
            }
        });
    }

    /**
     * Sets up a button with the given icon and action command
     *
     * @param icon icon for the button
     * @param actionCommand action command for the button
     */
    private void setUpButton(BufferedImage icon, String actionCommand) {
        JButton button = new JButton(new ImageIcon(icon));
        button.setActionCommand(actionCommand);
        button.addActionListener(this);
        toolPanel.add(button);
    }

    /**
     * Handles creation of a new map
     */
    private void handleNewMap() {
        initializeVariables();
        terrainPanel.setSize(terrainPanel.getPreferredSize());
        pack();
    }

    /**
     * Handles renaming the current map
     */
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

    /**
     * Handles resizing the map.
     * The following code is modeled after <a href="https://stackoverflow.com/questions/11211286/">...</a>
     */
    private void handleResizeMap() {
        TerrainResizeDialog terrainResizeDialog = new TerrainResizeDialog(this);
        terrainResizeDialog.setVisible(true);
        if (!terrainResizeDialog.isCancelled()) {
            try {
                int newWidth = terrainResizeDialog.getNewWidth();
                int newHeight = terrainResizeDialog.getNewHeight();
                currentTerrain.resize(newWidth, newHeight);
                terrainPanel.setSize(terrainPanel.getPreferredSize());
                pack();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Please enter an integer for both width and height.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this,
                        "Width must be at least 15 and height must be at least 10",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Saves the terrain to json based on user input.
     * Code modeled after <a href="https://stackoverflow.com/questions/17010647/set-default-saving-extension-with-jfilechooser">...</a>
     * and <a href="https://stackoverflow.com/questions/3571223/how-do-i-get-the-file-extension-of-a-file-in-java">...</a>
     */
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

    /**
     * Writes the current terrain to json at the given path.
     *
     * @param path The path to where the file is to be saved, including filename and extension
     */
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

    /**
     * Loads a new map based on user input
     * Code modeled after method from <a href="https://www.geeksforgeeks.org/java-swing-jfilechooser/">...</a>
     */
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

    /**
     * Inspects and modifies units based on user input
     * Code modeled after <a href="https://stackoverflow.com/questions/7855227/wait-for-jdialog-to-close">...</a>
     *
     * @param mapX Terrain x coordinate of unit
     * @param mapY Terrain y coordinate of unit
     */
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

    /**
     * Handles the click of the place unit button
     */
    private void handlePlaceUnitButton() {
        UnitEditorDialog unitEditorDialog = new UnitEditorDialog(this, factionToAdd, battleClassToAdd);
        unitEditorDialog.setVisible(true);
        if (!unitEditorDialog.isCancelled()) {
            factionToAdd = unitEditorDialog.getFaction();
            battleClassToAdd = unitEditorDialog.getBattleClass();
            this.currentEditMode = EditMode.PLACE_UNIT;
        }
    }

    /**
     * Places unit on the terrain based on user input
     *
     * @param mapX Terrain x coordinate of click
     * @param mapY Terrain y coordinate of click
     */
    void handlePlaceUnit(int mapX, int mapY) {
        currentTerrain.addUnit(new Unit(factionToAdd, battleClassToAdd, mapX, mapY));
    }

    /**
     * Deletes unit based on user input
     *
     * @param mapX Terrain x coordinate of click
     * @param mapY Terrain y coordinate of click
     */
    void handleDeleteUnit(int mapX, int mapY) {
        currentTerrain.deleteUnit(mapX, mapY);
    }

    /**
     * Handles changes to the Terrain based on clicks occurring on the map
     *
     * @param mapX Terrain x coordinate of click
     * @param mapY Terrain y coordinate of click
     * @param dragged if the click was a drag or not
     */
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

    /**
     * Returns the current terrain
     *
     * @return the current Terrain
     */
    Terrain getCurrentTerrain() {
        return currentTerrain;
    }

    /**
     * Returns the image of the given faction unit marker
     *
     * @param faction the faction of the desired image
     * @return image of the unit marker
     */
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

    /**
     * Returns the image corresponding to the terrain tile type.
     *
     * @param tileType the TerrainTile corresponding to the desired image
     * @return the desired image
     */
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

}