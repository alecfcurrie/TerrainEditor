package ui;

import model.Terrain;
import model.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static ui.TerrainEditorFrame.IMAGE_SIZE;

/**
 * Represents a panel that displays a Terrain object
 */
class TerrainPanel extends JPanel {

    private TerrainEditorFrame owner;

    /**
     * Constructs a new TerrainPanel
     *
     * @param owner the owner of this
     */
    public TerrainPanel(TerrainEditorFrame owner) {
        this.owner = owner;

        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                handleClick(e.getX(), e.getY(), false);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                handleClick(e.getX(), e.getY(), true);
            }
        });
    }

    /**
     * Draws the owner's terrain on the panel
     *
     * @param g graphics object that will draw the panel
     */
    private void drawMap(Graphics g) {
        Terrain currentTerrain = owner.getCurrentTerrain();
        int width = currentTerrain.getWidth();
        int height = currentTerrain.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                g.drawImage(owner.getImageOfTerrain(currentTerrain.getTileType(i, j)),
                        i * IMAGE_SIZE, j * IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE, null);
            }
        }

        List<Unit> units = currentTerrain.getUnits();

        for (Unit unit : units) {
            g.drawImage(owner.getImageOfUnit(unit.getFaction()), unit.getX() * IMAGE_SIZE,
                    unit.getY() * IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE, null);
        }
    }

    /**
     * Handles a click on the map
     *
     * @param x the screen x coordinate of the click
     * @param y the screen y coordinate of the click
     * @param dragged whether the click was dragged or not
     */
    private void handleClick(int x, int y, boolean dragged) {
        int terrainX = x / IMAGE_SIZE;
        int terrainY = y / IMAGE_SIZE;
        owner.handleClick(terrainX, terrainY, dragged);
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        Terrain currentTerrain = owner.getCurrentTerrain();
        return new Dimension(currentTerrain.getWidth() * IMAGE_SIZE, currentTerrain.getHeight() * IMAGE_SIZE);
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMap(g);
    }
}