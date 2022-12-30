package persistence;


import model.*;
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
            Terrain terrain = reader.readTerrain();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }
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
