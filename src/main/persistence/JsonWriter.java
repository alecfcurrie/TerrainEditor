package persistence;

import model.Terrain;
import model.TerrainList;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Represents a writer that writes a JSON representation of a MapList object to file.
// Modeled after code from JsonWriter class in:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of a MapList object to file
    public void write(TerrainList terrainList) {
        JSONObject json = terrainList.toJson();
        saveToFile(json.toString(TAB));
    }

    public void write(Terrain terrain) {
        JSONObject json = terrain.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }

}