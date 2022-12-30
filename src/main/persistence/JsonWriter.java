package persistence;

import model.Terrain;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Represents a writer that writes a JSON representation of a MapList object to file.
 * Modeled after code from JsonWriter class in:
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    /**
     * Constructs a writer that writes to the given destination
     *
     * @param destination path where the terrain is being written, including filename and extension
     */
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    /**
     * Writes Terrain to file if this is open
     *
     * @param terrain Terrain to be written to file
     */
    public void write(Terrain terrain) {
        JSONObject json = terrain.toJson();
        saveToFile(json.toString(TAB));
    }

    /**
     * Opens writer
     *
     * @throws FileNotFoundException destination file cannot be opened for writing
     */
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    /**
     * Closes writer
     */
    public void close() {
        writer.close();
    }

    /**
     * Writes string to file
     *
     * @param json string to be written to file
     */
    private void saveToFile(String json) {
        writer.print(json);
    }

}