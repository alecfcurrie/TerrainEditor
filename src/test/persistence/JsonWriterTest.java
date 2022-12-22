package persistence;

import model.*;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// Tests functionality of the JsonWriter
// Code modeled after methods from the JsonWriterTest class in:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonWriterTest {

    @Test
    void testWriterInvalidPath() {
        try {
            JsonWriter writer = new JsonWriter("./data//?*<>IllegalName.json");
            writer.open();
            fail("IOException expected.");
        } catch (FileNotFoundException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyMapList() {
        try {
            String source = "./data/testWriterEmptyMaplist.json";
            TerrainList terrainList = new TerrainList();
            JsonWriter writer = new JsonWriter(source);
            writer.open();
            writer.write(terrainList);
            writer.close();

            JsonReader reader = new JsonReader(source);
            terrainList = reader.readTerrainList();
            assertEquals(0, terrainList.size());
        } catch (IOException e) {
            fail("Unexpected IO error");
        }
    }

    @Test
    void testWriterGeneralMapList() {
        TerrainList terrainList = new TerrainList();

        Terrain testTerrain1 = new Terrain("Chapter 1", 15, 10);
        testTerrain1.addUnit(new Unit(Faction.PLAYER, BattleClass.CAVALIER, 4, 2));
        terrainList.add(testTerrain1);
        Terrain testTerrain2 = new Terrain("Chapter 2", 20, 25);
        testTerrain2.setTile(TerrainTile.MOUNTAIN, 3, 7);
        terrainList.add(testTerrain2);
        try {
            String source = "./data/testWriterGeneralMaplist.json";
            JsonWriter writer = new JsonWriter(source);
            writer.open();
            writer.write(terrainList);
            writer.close();

            JsonReader reader = new JsonReader(source);
            terrainList = reader.readTerrainList();

        } catch (IOException e) {
            fail("Unexpected IO error");
        }

        assertEquals(2, terrainList.size());
        Terrain newTerrain1 = terrainList.get(0);
        Terrain newTerrain2 = terrainList.get(1);

        assertEquals("Chapter 1", newTerrain1.getName());
        assertEquals(testTerrain1.getWidth(), newTerrain1.getWidth());
        assertEquals(testTerrain1.getHeight(), newTerrain1.getHeight());
        TestTerrain.allTilesPlain(newTerrain1);
        Unit unit = newTerrain1.getUnit(4, 2);
        assertNotNull(unit);
        assertEquals(BattleClass.CAVALIER, unit.getBattleClass());
        assertEquals(Faction.PLAYER, unit.getFaction());

        assertEquals("Chapter 2", newTerrain2.getName());
        assertEquals(testTerrain2.getWidth(), newTerrain2.getWidth());
        assertEquals(testTerrain2.getHeight(), newTerrain2.getHeight());
        assertEquals(0, newTerrain2.getUnits().size());

        for (int i = 0; i < newTerrain2.getWidth(); i++) {
            for (int j = 0; j < newTerrain2.getHeight(); j++) {
                if (i == 3 && j == 7) {
                    assertEquals(TerrainTile.MOUNTAIN, newTerrain2.getTileType(i, j));
                } else {
                    assertEquals(TerrainTile.PLAIN, newTerrain2.getTileType(i, j));
                }
            }
        }

    }

    @Test
    void testMapWriter() {
        JsonWriter jsonWriter = new JsonWriter("./data/mapSaves/testWriterMap.json");
        JsonReader jsonReader = new JsonReader("./data/mapSaves/testWriterMap.json");
        Terrain testMap = new Terrain("JsonTestMap", 15, 10);
        try {
            jsonWriter.open();
            jsonWriter.write(testMap);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            fail("Unexpected write error");
        }

        try {
            Terrain readMap = jsonReader.readTerrain();
            assertEquals(testMap, readMap);
        } catch (IOException e) {
            fail("Unexpected read error");
        }
    }
}
