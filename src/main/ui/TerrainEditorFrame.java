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
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Represents a graphical interface to edit Terrain objects
 */
public class TerrainEditorFrame extends JFrame {

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
     * Initializes the menu bar
     */
    private void initializeMenuBar() {
        // Work around for space always being reserved for menu item icons in the Windows look and feel.
        // https://github.com/JFormDesigner/FlatLaf/issues/328
        UIManager.put("MenuItem.margin", new Insets(2, -25, 2, -5));

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = initializeFileMenu();
        JMenu editMenu = initializeEditMenu();

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        this.setJMenuBar(menuBar);
    }

    /**
     * Initializes the Edit menu
     *
     * @return the initialized Edit menu
     */
    private JMenu initializeEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        addMenuItem(editMenu, "Rename", (e -> handleRenameMap()));
        addMenuItem(editMenu, "Resize", (e -> handleResizeMap()));
        return editMenu;
    }

    /**
     * Initializes the File menu
     *
     * @return the initialized File menu
     */
    private JMenu initializeFileMenu() {
        JMenu fileMenu = new JMenu("File");

        addMenuItem(fileMenu, "New", (e -> handleNewMap()));
        addMenuItem(fileMenu, "Load", (e -> handleLoadMap()));
        addMenuItem(fileMenu, "Save", (e -> handleSaveMap()));
        fileMenu.addSeparator();
        // from https://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe
        addMenuItem(fileMenu, "Exit", (e -> this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING))));
        return fileMenu;
    }

    /**
     * Adds a menu item to the given menu
     *
     * @param menu The {@link JMenu} to add the {@link JMenuItem} to
     * @param menuText The text of the {@link JMenuItem}
     * @param ae A lambda expression that accepts an {@link ActionEvent}, and calls the desired
     *           function to handle the event.
     */
    private void addMenuItem(JMenu menu, String menuText, Consumer<ActionEvent> ae) {
        JMenuItem menuItem = new JMenuItem(menuText);
        menuItem.addActionListener(e -> ae.accept(e));
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
        setUpButton(plainImg, (e -> currentEditMode = EditMode.PLACE_PLAIN));
        setUpButton(mountainImg, (e -> currentEditMode = EditMode.PLACE_MOUNTAIN));
        setUpButton(forestImg, (e -> currentEditMode = EditMode.PLACE_FOREST));
        setUpButton(waterImg, (e -> currentEditMode = EditMode.PLACE_WATER));
        setUpButton(wallImg, (e -> currentEditMode = EditMode.PLACE_WALL));
        setUpButton(chestImg, (e -> currentEditMode = EditMode.PLACE_CHEST));
        setUpButton(gateImg, (e -> currentEditMode = EditMode.PLACE_GATE));
        setUpButton(throneImg, (e -> currentEditMode = EditMode.PLACE_THRONE));
        setUpButton(addUnitImg, (e -> handlePlaceUnitButton()));
        setUpButton(inspectUnitImg, (e -> currentEditMode = EditMode.INSPECT_UNIT));
        setUpButton(deleteUnitImg, (e -> currentEditMode = EditMode.DELETE_UNIT));
    }

    /**
     * Sets the close operation for the TerrainEditorFrame
     * Modeled after method from <a href="https://www.clear.rice.edu/comp310/JavaResources/frame_close.html">...</a>
     */
    private void setCloseOperation() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * Sets up a button with the given icon and action command
     *
     * @param icon icon for the button
     * @param ae A lambda expression that takes an {@link ActionEvent} and calls the desired
     *           function to handle the event
     */
    private void setUpButton(BufferedImage icon, Consumer<ActionEvent> ae) {
        JButton button = new JButton(new ImageIcon(icon));
        button.addActionListener(e -> ae.accept(e));
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
        return switch (faction) {
            case PLAYER -> playerImg;
            case ENEMY -> enemyImg;
            default -> allyImg;
        };
    }

    /**
     * Returns the image corresponding to the terrain tile type.
     *
     * @param tileType the TerrainTile corresponding to the desired image
     * @return the desired image
     */
    BufferedImage getImageOfTerrain(TerrainTile tileType) {
        return switch (tileType) {
            case PLAIN -> plainImg;
            case MOUNTAIN -> mountainImg;
            case FOREST -> forestImg;
            case WATER -> waterImg;
            case WALL -> wallImg;
            case CHEST -> chestImg;
            case GATE -> gateImg;
            default -> throneImg;
        };
    }

}