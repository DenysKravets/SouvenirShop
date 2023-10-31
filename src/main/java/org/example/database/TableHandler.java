package org.example.database;

import org.example.entity.JSONable;
import org.example.factory.FromJSONFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class TableHandler<T extends JSONable> {

    private final String filename;
    private final FromJSONFactory<T> factory;

    public TableHandler(String filename, FromJSONFactory<T> factory) {
        this.filename = filename;
        this.factory = factory;
    }

    List<T> loadAll() {
        return factory.createAll(loadFile());
    }

    // Unconditional save, doesn't contain logic
    void saveAll(List<T> entities) {;
        JSONArray jsonArray = new JSONArray();
        for (T t : entities) {
            jsonArray.put(new JSONObject(t.toJSON()));
        }
        writeFile(jsonArray.toString());
    }

    protected String loadFile() {

        if (!new File(this.filename).exists())
            writeFile("[]");

        Path path = Paths.get(this.filename);
        try {
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void writeFile(String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.filename));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
