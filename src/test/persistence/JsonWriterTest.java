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
