package model;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

// Tests all functionality of the Map class.
// Class is public as JsonWriterTest uses a static helper method of this class.
public class TestTerrain {

    Terrain testTerrain1;
    Terrain testTerrain2;

    int testTerrain1InitialWidth;
    int testTerrain1InitialHeight;

    Unit mars;
    Unit alm;
    Unit celica;

    @BeforeEach
    void setup() {
        initializeMaps();

        mars = new Unit(Faction.PLAYER, BattleClass.THIEF, 5, 2);
        alm = new Unit(Faction.ENEMY, BattleClass.LORD, 6, 7);
        celica = new Unit(Faction.ALLY, BattleClass.CAVALIER, 7, 5);

        testTerrain1InitialWidth = testTerrain1.getWidth();
        testTerrain1InitialHeight = testTerrain1.getHeight();
    }

    private void initializeMaps() {
        try {
            testTerrain1 = new Terrain("Chapter 1", 15, 10);
            testTerrain2 = new Terrain("Chapter 2", 20, 30);
        } catch (IllegalArgumentException e){
            fail("Unexpected instantiation failure");
        }
    }

    public static void allTilesPlain(Terrain terrain) {
        int width = terrain.getWidth();
        int height = terrain.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                assertEquals(TerrainTile.PLAIN, terrain.getTileType(i, j));
            }
        }
    }

    @Test
    void testConstructor() {
        assertEquals("Chapter 1", testTerrain1.getName());
        assertEquals("Chapter 2", testTerrain2.getName());

        assertEquals(15, testTerrain1.getWidth());
        assertEquals(20, testTerrain2.getWidth());

        assertEquals(10, testTerrain1.getHeight());
        assertEquals(30, testTerrain2.getHeight());
    }

    @Test
    void testInitialStateOfTiles() {
        allTilesPlain(testTerrain1);
        allTilesPlain(testTerrain2);
    }

    @Test
    void testSecondConstructor() {
        UnitList units = new UnitList();
        units.add(mars);
        TerrainTile[][] newTerrainTile = new TerrainTile[15][10];
        setAllMountain(newTerrainTile);
        Terrain constructor2Test;
        try {
            constructor2Test = new Terrain("Secondo", newTerrainTile, units);
            assertEquals("Secondo", constructor2Test.getName());
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 10; j++) {
                    assertEquals(TerrainTile.MOUNTAIN, constructor2Test.getTileType(i, j));
                }
            }
            List<Unit> constructedUnits = constructor2Test.getUnits();
            assertEquals(1, constructedUnits.size());
            assertEquals(mars, constructedUnits.get(0));
        } catch (IllegalArgumentException e){
            fail("Unexpected instantiation failure");
        }
    }

    private static void setAllMountain(TerrainTile[][] newTerrainTile) {
        int width = newTerrainTile.length;
        int height = newTerrainTile[0].length;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                newTerrainTile[i][j] = TerrainTile.MOUNTAIN;
            }
        }
    }

    @Test
    void testIllegalTerrains() {
        testIllegalTerrainFirstConstructor(10, 5);
        testIllegalTerrainFirstConstructor(20, 5);
        testIllegalTerrainFirstConstructor(10, 20);
        testIllegalTerrainSecondConstructor(10,5);
        testIllegalTerrainSecondConstructor(20,5);
        testIllegalTerrainSecondConstructor(10,20);
    }

    private static void testIllegalTerrainFirstConstructor(int width, int height) {
        try {
            Terrain t = new Terrain("Test", width, height);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    private static void testIllegalTerrainSecondConstructor(int width, int height) {
        TerrainTile[][] terrainTiles = new TerrainTile[width][height];
        setAllMountain(terrainTiles);
        try {
            Terrain t = new Terrain("Test", terrainTiles, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    void testRename() {
        //Model tests
        String newName1 = "Prologue";
        String newName2 = "Chapter 1";
        String newName3 = "Premonition";
        testTerrain1.rename(newName1);
        testTerrain2.rename(newName2);
        assertEquals(newName1, testTerrain1.getName());
        assertEquals(newName2, testTerrain2.getName());
        testTerrain1.rename(newName3);
        assertEquals(newName3, testTerrain1.getName());
    }

    @Test
    void testSetTileSimple() {
        TerrainTile terrainTile = TerrainTile.MOUNTAIN;
        int x = 5;
        int y = 4;
        assertTrue(testTerrain1.setTile(terrainTile, x, y));
        int width = testTerrain1.getWidth();
        int height = testTerrain1.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if ((i == x) && (j == y)) {
                    assertEquals(TerrainTile.MOUNTAIN, testTerrain1.getTileType(i, j));
                } else {
                    assertEquals(TerrainTile.PLAIN, testTerrain1.getTileType(i, j));
                }
            }
        }
    }
    private void modifyTiles() {
        assertTrue(testTerrain1.setTile(TerrainTile.WATER, 0, 3));
        assertTrue(testTerrain1.setTile(TerrainTile.WATER, 1, 3));
        assertTrue(testTerrain1.setTile(TerrainTile.WATER, 1, 2));
        assertTrue(testTerrain1.setTile(TerrainTile.WATER, 2, 2));
        assertTrue(testTerrain1.setTile(TerrainTile.WATER, 3, 2));
        assertTrue(testTerrain1.setTile(TerrainTile.WATER, 3, 1));
        assertTrue(testTerrain1.setTile(TerrainTile.WATER, 3, 0));
    }

    @Test
    void testSetTilesComplex() {
        modifyTiles();
        int width = testTerrain1.getWidth();
        int height = testTerrain1.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if ((j == 3 && i <= 1) || (j == 2 && i >= 1 && i <= 3) || (i == 3 && j <= 2)) {
                    assertEquals(TerrainTile.WATER, testTerrain1.getTileType(i, j));
                } else {
                    assertEquals(TerrainTile.PLAIN, testTerrain1.getTileType(i, j));
                }
            }
        }
    }

    @Test
    void testResizeNoChange() {
        int width = 20;
        int height = 25;
        testTerrain1.resize(width, height);
        assertEquals(width, testTerrain1.getWidth());
        assertEquals(height, testTerrain1.getHeight());
        allTilesPlain(testTerrain1);
    }

    @Test
    void testResizeSimpleChange() {
        testTerrain1.setTile(TerrainTile.THRONE, 6, 4);
        testTerrain1.resize(20, 25);
        assertEquals(20, testTerrain1.getWidth());
        assertEquals(25, testTerrain1.getHeight());
        int width = testTerrain1.getWidth();
        int height = testTerrain1.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if ((i == 6) && (j == 4)) {
                    assertEquals(TerrainTile.THRONE, testTerrain1.getTileType(i, j));
                } else {
                    assertEquals(TerrainTile.PLAIN, testTerrain1.getTileType(i, j));
                }
            }
        }
    }

    @Test
    void testResizeComplexChange() {
        testTerrain1.setTile(TerrainTile.FOREST, 6, 4);
        testTerrain1.setTile(TerrainTile.WALL, 7, 4);
        testTerrain1.setTile(TerrainTile.CHEST, 9, 5);
        testTerrain1.resize(20, 25);
        testTerrain1.setTile(TerrainTile.GATE, 19, 23);
        assertEquals(20, testTerrain1.getWidth());
        assertEquals(25, testTerrain1.getHeight());
        int width = testTerrain1.getWidth();
        int height = testTerrain1.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if ((i == 6) && (j == 4)) {
                    assertEquals(TerrainTile.FOREST, testTerrain1.getTileType(i, j));
                } else if ((i == 7) && (j == 4)) {
                    assertEquals(TerrainTile.WALL, testTerrain1.getTileType(i, j));
                } else if ((i == 9) && (j == 5)) {
                    assertEquals(TerrainTile.CHEST, testTerrain1.getTileType(i, j));
                } else if ((i == 19) && (j == 23)) {
                    assertEquals(TerrainTile.GATE, testTerrain1.getTileType(i, j));
                } else {
                    assertEquals(TerrainTile.PLAIN, testTerrain1.getTileType(i, j));
                }
            }
        }
    }

    @Test
    void testResizeShrink() {
        testTerrain2.setTile(TerrainTile.MOUNTAIN, 17, 15);
        testTerrain2.resize(15, 10);
        allTilesPlain(testTerrain2);
    }

    @Test
    void testResizeShrinkBoundary() {
        testTerrain2.setTile(TerrainTile.MOUNTAIN, 15, 10);
        testTerrain2.resize(15, 10);
        allTilesPlain(testTerrain2);
    }

    @Test
    void testResizeShrinkComplex() {
        testTerrain2.setTile(TerrainTile.CHEST, 18, 4);
        testTerrain2.setTile(TerrainTile.CHEST, 10, 5);
        testTerrain2.resize(15, 10);
        int width = testTerrain2.getWidth();
        int height = testTerrain2.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if ((i == 10) && (j == 5)) {
                    assertEquals(TerrainTile.CHEST, testTerrain2.getTileType(i, j));
                } else {
                    assertEquals(TerrainTile.PLAIN, testTerrain2.getTileType(i, j));
                }
            }
        }
    }

    @Test
    void testIllegalResizes() {
        testIllegalResize(10, 5);
        testIllegalResize(5, 20);
        testIllegalResize(20, 5);
    }

    private void testIllegalResize(int width, int height) {
        try {
            testTerrain1.resize(width, height);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(testTerrain1InitialHeight, testTerrain1.getHeight());
            assertEquals(testTerrain1InitialWidth, testTerrain1.getWidth());
        }
    }

    @Test
    void testAddUnit() {
        assertTrue(testTerrain1.addUnit(mars));
        List<Unit> units = testTerrain1.getUnits();
        assertEquals(1, units.size());
        assertEquals(mars, units.get(0));
        assertTrue(testTerrain1.addUnit(alm));
        assertEquals(2, units.size());
        assertEquals(mars, units.get(0));
        assertEquals(alm, units.get(1));
    }

    @Test
    void testAddUnitBlocked() {
        assertTrue(testTerrain1.addUnit(mars));
        Unit gordin = new Unit(Faction.PLAYER, BattleClass.ARCHER, mars.getX(), mars.getY());
        assertFalse(testTerrain1.addUnit(gordin));
        List<Unit> units = testTerrain1.getUnits();
        assertEquals(1, units.size());
        assertEquals(mars, units.get(0));
    }

    @Test
    void testRemoveUnit() {
        testTerrain1.addUnit(mars);
        assertTrue(testTerrain1.deleteUnit(mars.getX(), mars.getY()));
        List<Unit> units = testTerrain1.getUnits();
        assertEquals(0, units.size());
        testTerrain1.addUnit(alm);
        testTerrain1.addUnit(celica);
        assertTrue(testTerrain1.deleteUnit(alm.getX(), alm.getY()));
        assertEquals(1, units.size());
        assertEquals(celica, units.get(0));
        testTerrain1.deleteUnit(celica.getX(), celica.getY());
        assertEquals(0, units.size());
    }

    @Test
    void testRemoveUnoccupiedTile() {
        testTerrain1.addUnit(mars);
        assertFalse(testTerrain1.deleteUnit(mars.getX() - 1, mars.getY() - 1));

        List<Unit> units = testTerrain1.getUnits();
        assertEquals(1, units.size());
        assertEquals(mars, units.get(0));

        assertFalse(testTerrain1.deleteUnit(mars.getX(), mars.getY() - 1));
        assertEquals(1, units.size());
        assertEquals(mars, units.get(0));

        assertFalse(testTerrain1.deleteUnit(mars.getX() - 1, mars.getY()));
        assertEquals(1, units.size());
        assertEquals(mars, units.get(0));
    }

    @Test
    void testResizeRemoveUnits() {
        testTerrain2.addUnit(mars);
        Unit cain = new Unit(Faction.PLAYER, BattleClass.CAVALIER, testTerrain2.getWidth() - 1, testTerrain2.getHeight() - 1);
        Unit abel = new Unit(Faction.PLAYER, BattleClass.CAVALIER, 15, 10);
        Unit jagen = new Unit(Faction.PLAYER, BattleClass.CAVALIER, 14, 10);
        Unit matthis = new Unit(Faction.PLAYER, BattleClass.CAVALIER, 15, 9);
        Unit luke = new Unit(Faction.PLAYER, BattleClass.CAVALIER, 16, 9);
        Unit roderick = new Unit(Faction.PLAYER, BattleClass.CAVALIER, 8, 15);
        testTerrain2.addUnit(cain);
        testTerrain2.addUnit(abel);
        testTerrain2.addUnit(jagen);
        testTerrain2.addUnit(matthis);
        testTerrain2.addUnit(luke);
        testTerrain2.addUnit(roderick);
        testTerrain2.resize(15, 10);
        List<Unit> units = testTerrain2.getUnits();
        assertEquals(1, units.size());
        assertEquals(mars, units.get(0));
    }

    @Test
    void testGetUnit() {
        testTerrain1.addUnit(mars);
        assertEquals(mars, testTerrain1.getUnit(mars.getX(), mars.getY()));
    }

    @Test
    void testGetUnitUnoccupied() {
        testTerrain1.addUnit(mars);
        assertNull(testTerrain1.getUnit(mars.getX() + 1, mars.getY() + 1));
        assertNull(testTerrain1.getUnit(mars.getX(), mars.getY() + 1));
        assertNull(testTerrain1.getUnit(mars.getX() + 1, mars.getY()));
    }

    @Test
    void testAddUnitOutOfBounds() {
        Unit u1 = new Unit(Faction.PLAYER, BattleClass.CAVALIER, -1, 5);
        Unit u2 = new Unit(Faction.PLAYER, BattleClass.CAVALIER, 1, -1);
        Unit u3 = new Unit(Faction.PLAYER, BattleClass.CAVALIER, testTerrain1.getWidth(), 5);
        Unit u4 = new Unit(Faction.PLAYER, BattleClass.CAVALIER, 5, testTerrain1.getHeight());
        assertFalse(testTerrain1.addUnit(u1));
        assertFalse(testTerrain1.addUnit(u2));
        assertFalse(testTerrain1.addUnit(u3));
        assertFalse(testTerrain1.addUnit(u4));
    }

    @Test
    void testSetTerrainOutOfBounds() {
        assertFalse(testTerrain1.setTile(TerrainTile.MOUNTAIN, -1, 5));
        assertFalse(testTerrain1.setTile(TerrainTile.MOUNTAIN, 7, -1));
        assertFalse(testTerrain1.setTile(TerrainTile.MOUNTAIN, testTerrain1.getWidth(), 5));
        assertFalse(testTerrain1.setTile(TerrainTile.MOUNTAIN, 7, testTerrain1.getHeight()));

    }

    @Test
    void testEqualsSameObject() {
        assertEquals(testTerrain1, testTerrain1);
    }

    @Test
    void testEqualsSameParameters() {
        setTerrainsEqual();
        testTerrain1.setTile(TerrainTile.MOUNTAIN, 5, 7);
        testTerrain2.setTile(TerrainTile.MOUNTAIN, 5, 7);
        testTerrain1.addUnit(mars);
        testTerrain2.addUnit(new Unit(mars.getFaction(), mars.getBattleClass(), mars.getX(), mars.getY()));
        assertEquals(testTerrain1, testTerrain2);
    }

    @Test
    void testEqualsNonIdenticalObjectTypes() {
        assertNotEquals(testTerrain1, null);
        assertNotEquals(testTerrain1, mars);
    }

    @Test
    void testEqualsDifferentParameters() {
        setTerrainsEqual();
        testTerrain2.setTile(TerrainTile.MOUNTAIN, 5, 7);
        assertNotEquals(testTerrain1, testTerrain2);
        setTerrainsEqual();
        testTerrain2.rename("SomeNewName");
        assertNotEquals(testTerrain1, testTerrain2);
        setTerrainsEqual();
        testTerrain2.addUnit(mars);
        assertNotEquals(testTerrain1, testTerrain2);
    }

    private void setTerrainsEqual() {
        testTerrain2 = new Terrain(testTerrain1.getName(), testTerrain1.getWidth(), testTerrain1.getHeight());
        assertEquals(testTerrain1, testTerrain2);
    }

    @Test
    void testHashCode() {
        assertEquals(Objects.hash(testTerrain1.getName()), testTerrain1.hashCode());
    }

}