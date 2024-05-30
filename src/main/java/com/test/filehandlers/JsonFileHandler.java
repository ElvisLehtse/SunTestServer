package com.test.filehandlers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class, JsonFileHandler, handles parsing, modifying, and deleting JSON objects related to weapons.
 * It provides methods to read from and write to a JSON file, as well as to add, modify, or delete weapon information.
 */
public class JsonFileHandler implements FileHandler {

    private JSONArray writeToJsonArray = new JSONArray();

    /**
     * Reads the contents of the JSON file and returns them as a string.
     * @param filePath Specifies the in file being read.
     * @return The contents of the JSON file as a string.
     */
    public String read(Path filePath) throws IOException {
        return Files.readString(filePath);
    }

    /**
     * Adds a new weapon to the JSON file.
     * @param requestBody Weapon's data specified by the user.
     * @param filePath Specifies the file in which data is being read and written.
     */
    public void add(String requestBody, Path filePath) throws IOException {
        String fileContent = Files.readString(filePath);
        writeToJsonArray = new JSONArray(fileContent);
        JSONObject weaponObject = new JSONObject(requestBody);
        writeToJsonArray.put(weaponObject);
        writeFile(filePath);
    }

    /**
     * Modifies an existing weapon in the JSON file.
     * @param oldName The name of the weapon to be modified.
     * @param requestBody Weapon's data specified by the user.
     * @param filePath Specifies the file in which data is being read and written.
     */
    public void modify(String oldName, String requestBody, Path filePath) throws IOException {
        String fileContent = Files.readString(filePath);
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
        writeFile(filePath);
    }

    /**
     * Deletes an existing weapon from the JSON file.
     * @param weaponsName The name of the weapon to be deleted.
     * @param filePath Specifies the file in which data is being read and written.
     */
    public void delete(String weaponsName, Path filePath) throws IOException {
        String fileContent = Files.readString(filePath);
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
        writeFile(filePath);
    }

    /**
     * Resets the JSON file to its initial state with predefined weapons.
     * @param filePath Specifies the file in which data is being written.
     * @param resetFilePath Specifies the file in which data is being read.
     */
    public void reset(Path filePath, Path resetFilePath) throws IOException {
        String fileContent = Files.readString(resetFilePath);
        writeToJsonArray = new JSONArray(fileContent);
        writeFile(filePath);
    }

    /**
     * Writes the contents of the JSON array to the JSON file.
     * @param filePath Specifies the file in which data is being written.
     */
    private void writeFile(Path filePath) throws IOException {
        final int prettyPrintJson = 4;
        FileWriter myWriter = new FileWriter(String.valueOf(filePath));
        String writeString = writeToJsonArray.toString(prettyPrintJson);
        myWriter.write(writeString);
        myWriter.flush();
        myWriter.close();
    }
}

