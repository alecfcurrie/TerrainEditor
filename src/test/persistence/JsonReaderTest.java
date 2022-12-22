package persistence;


import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// Tests functionality of the JsonReader
// Code modeled after methods from JsonReaderTest in:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/nonexistentFile.json");
        try {
            TerrainList terrainList = reader.readTerrainList();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderGeneralMap() {
        JsonReader reader = new JsonReader("./data/maplistSimpleMap.json");
        try {
            TerrainList terrainList = reader.readTerrainList();
            assertEquals(1, terrainList.size());
            Terrain terrain = terrainList.get(0);
            assertEquals(15, terrain.getWidth());
            assertEquals(10, terrain.getHeight());
            assertEquals("Prologue", terrain.getName());
            assertEquals(1, terrain.getUnits().size());
            Unit unit = terrain.getUnit(10, 5);
            assertNotNull(unit);
            Assertions.assertEquals(Faction.PLAYER, unit.getFaction());
            Assertions.assertEquals(BattleClass.LORD, unit.getBattleClass());
            assertTerrainSimpleMap(terrain);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderComplex() {
        JsonReader reader = new JsonReader("./data/maplistMultipleMaps.json");
        try {
            TerrainList terrainList = reader.readTerrainList();
            assertEquals(2, terrainList.size());
            Terrain ch1 = terrainList.get(0);
            Terrain ch2 = terrainList.get(1);

            //Chapter 1 tests
            assertEquals("Chapter 1", ch1.getName());
            assertEquals(20, ch1.getWidth());
            assertEquals(13, ch1.getHeight());
            assertEquals(2, ch1.getUnits().size());
            Unit unit1 = ch1.getUnit(5, 5);
            assertNotNull(unit1);
            assertEquals(Faction.PLAYER, unit1.getFaction());
            assertEquals(BattleClass.THIEF, unit1.getBattleClass());
            Unit unit2 = ch1.getUnit(7, 2);
            assertNotNull(unit2);
            assertEquals(Faction.ENEMY, unit2.getFaction());
            assertEquals(BattleClass.CAVALIER, unit2.getBattleClass());
            testAllTerrainCh1(ch1);

            //Chapter 2 tests
            assertEquals("Chapter 2", ch2.getName());
            assertEquals(17, ch2.getWidth());
            assertEquals(12, ch2.getHeight());
            assertEquals(1, ch2.getUnits().size());
            Unit unit3 = ch2.getUnit(11, 7);
            assertNotNull(unit3);
            assertEquals(Faction.ALLY, unit3.getFaction());
            assertEquals(BattleClass.FIGHTER, unit3.getBattleClass());
            testAllTerrainCh2(ch2);

        } catch (IOException e) {
            fail("Unexpected read error");
        }
    }

    @Test
    void testReaderEmpty() {
        JsonReader reader = new JsonReader("./data/maplistEmpty.json");
        try {
            TerrainList terrainList = reader.readTerrainList();
            assertEquals(0, terrainList.size());
        } catch (IOException e) {
            fail("Unexpected read error");
        }
    }

    // EFFECTS: Checks the expected terrain for chapter 1
    private void testAllTerrainCh1(Terrain terrain) {
        for (int i = 0; i < terrain.getWidth(); i++) {
            for (int j = 0; j < terrain.getHeight(); j++) {
                if (i == 0 && j == 0) {
                    assertEquals(TerrainTile.CHEST, terrain.getTileType(i, j));
                } else if (i == 1 && j == 0) {
                    assertEquals(TerrainTile.WALL, terrain.getTileType(i, j));
                } else {
                    assertEquals(TerrainTile.PLAIN, terrain.getTileType(i, j));
                }
            }
        }
    }

    // EFFECTS: Tests the expected terrain for chapter 2
    private void testAllTerrainCh2(Terrain terrain) {
        for (int i = 0; i < terrain.getWidth(); i++) {
            for (int j = 0; j < terrain.getHeight(); j++) {
                if (i == 7 && j == 8) {
                    assertEquals(TerrainTile.WATER, terrain.getTileType(i, j));
                } else {
                    assertEquals(TerrainTile.PLAIN, terrain.getTileType(i, j));
                }
            }
        }
    }

    // EFFECTS: Checks the expected terrain for the simple map (Prologue)
    private void assertTerrainSimpleMap(Terrain terrain) {
        for (int i = 0; i < terrain.getWidth(); i++) {
            for (int j = 0; j < terrain.getHeight(); j++) {
                if (i == 1 && j == 0) {
                    assertEquals(TerrainTile.MOUNTAIN, terrain.getTileType(i, j));
                } else {
                    assertEquals(TerrainTile.PLAIN, terrain.getTileType(i, j));
                }
            }
        }
    }

    // EFFECTS: Tests the direct map read of the JSONReader
    @Test
    void testMapReader() {
        JsonReader jsonReader = new JsonReader("./data/mapSaves/ConvoyAmbush.json");
        try {
            Terrain testMap = jsonReader.readTerrain();
            // Test suite is non-exhaustive, as it reuses code that the above tests already test
            assertEquals("Convoy Ambush", testMap.getName());
            assertEquals(10, testMap.getUnits().size());
            assertEquals(TerrainTile.WATER, testMap.getTileType(10, 4));
        } catch (IOException e) {
            fail("Unexpected Exception thrown");
        }
    }

}
