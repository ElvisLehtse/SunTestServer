package com.test;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class, Json_Parser, handles parsing, modifying, and deleting JSON objects related to weapons.
 * It provides methods to read from and write to a JSON file, as well as to add, modify, or delete weapon information.
 */
class JsonFileHandler implements FileHandler {
    private final String fileName = "text.json";
    private JSONArray writeToJsonArray = new JSONArray();

    /**
     * Reads the contents of the JSON file and returns them as a string.
     * @return The contents of the JSON file as a string.
     */
    public String read() throws IOException {
        return Files.readString(Path.of(fileName));
    }

    /**
     * Adds a new weapon to the JSON file.
     * @param requestBody Weapon's data specified by the user.
     */
    public void add(String requestBody) throws IOException {
        String fileContent = Files.readString(Path.of(fileName));
        writeToJsonArray = new JSONArray(fileContent);
        JSONObject weaponObject = new JSONObject(requestBody);
        writeToJsonArray.put(weaponObject);
        writeFile();
    }

    /**
     * Modifies an existing weapon in the JSON file.
     * @param oldName The name of the weapon to be modified.
     * @param requestBody Weapon's data specified by the user.
     */
    public void modify(String oldName, String requestBody) throws IOException {
        String fileContent = Files.readString(Path.of(fileName));
        JSONArray readFromJsonArray = new JSONArray(fileContent);
        writeToJsonArray.clear();
        boolean errorMessage = true;
        for (int i = 0; i < readFromJsonArray.length(); i++) {
            JSONObject weaponObject = (JSONObject) readFromJsonArray.get(i);

            if (weaponObject.has(oldName)) {
                weaponObject = new JSONObject(requestBody);
                errorMessage = false;
            } else if (errorMessage && i == readFromJsonArray.length() - 1) {
                throw new IllegalArgumentException();
            }
            writeToJsonArray.put(weaponObject);
        }
        writeFile();
    }

    /**
     * Deletes an existing weapon from the JSON file.
     * @param weaponsName The name of the weapon to be deleted.
     */
    public void delete(String weaponsName) throws IOException {
        String fileContent = Files.readString(Path.of(fileName));
        boolean errorMessage = true;
        JSONArray readFromJsonArray = new JSONArray(fileContent);
        writeToJsonArray.clear();
        for (int i = 0; i < readFromJsonArray.length(); i++) {
            JSONObject weaponObject = (JSONObject) readFromJsonArray.get(i);

            if (!weaponObject.has(weaponsName)) {
                writeToJsonArray.put(weaponObject);
                if (errorMessage && i == readFromJsonArray.length() - 1) {
                    throw new IllegalArgumentException();
                }
            } else if (weaponObject.has(weaponsName)) {
                errorMessage = false;
            }
        }
        writeFile();
    }

    /**
     * Resets the JSON file to its initial state with predefined weapons.
     */
    public void reset() throws IOException {
        String fileContent = Files.readString(Path.of("reset.json"));
        writeToJsonArray = new JSONArray(fileContent);
        writeFile();
    }

    /**
     * Writes the contents of the JSON array to the JSON file.
     */
    private void writeFile() throws IOException {
        final int prettyPrintJson = 4;
        FileWriter myWriter = new FileWriter(fileName);
        String writeString = writeToJsonArray.toString(prettyPrintJson);
        myWriter.write(writeString);
        myWriter.flush();
        myWriter.close();
    }
}

