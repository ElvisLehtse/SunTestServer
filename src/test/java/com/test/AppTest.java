package com.test;

import com.test.filehandlers.JsonFileHandler;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.*;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import org.junit.rules.TemporaryFolder;


public class AppTest
{
    JsonFileHandler jsonFileHandler = new JsonFileHandler();
    TestData testData = new TestData();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    @Before
    public void createFile() throws IOException {
        File tempFile = testFolder.newFile("test.json");
        File tempResetFile = testFolder.newFile("testReset.json");
        FileWriter fileWriter = new FileWriter(tempFile);
        fileWriter.write("[]");
        fileWriter.flush();
        fileWriter.close();
        setFilePath(tempFile.getAbsolutePath());
        setResetFilePath(tempResetFile.getAbsolutePath());
    }
    private String filePath;
    private String resetFilePath;
    private void setFilePath (String filePath) {
        this.filePath = filePath;
    }
    private String getFilePath () {
        return filePath;
    }
    private void setResetFilePath (String resetFilePath) {
        this.resetFilePath = filePath;
    }
    private String getResetFilePath () {
        return resetFilePath;
    }

    @Test
    public void read() throws IOException {
        String value = jsonFileHandler.read(Path.of(getFilePath()));
        assertEquals("[]", value);
    }

    @Test
    public void add() throws IOException {
        jsonFileHandler.add("{}", Path.of(getFilePath()));
        String value = jsonFileHandler.read(Path.of(getFilePath()));
        assertEquals("[{}]", value);
    }

    @Test
    public void deleteSuccess() throws IOException {
        jsonFileHandler.add(testData.testObject1(), Path.of(getFilePath()));
        jsonFileHandler.delete("testItem", Path.of(getFilePath()));
        String value = jsonFileHandler.read(Path.of(getFilePath()));
        assertEquals("[]", value);
    }

    @Test
    public void deleteFailure() throws IOException {
        jsonFileHandler.add(testData.testObject1(), Path.of(getFilePath()));
        try {
            jsonFileHandler.delete("incorrect name", Path.of(getFilePath()));
        } catch (IllegalArgumentException e) {
            String value = jsonFileHandler.read(Path.of(getFilePath()));
            assertEquals(STR."[\{testData.testObject1()}]", value);
        }
    }

    @Test
    public void modifySuccess() throws IOException {
        jsonFileHandler.add(testData.testObject1(), Path.of(getFilePath()));
        jsonFileHandler.modify("testItem", testData.testObject2(), Path.of(getFilePath()));
        String value = jsonFileHandler.read(Path.of(getFilePath()));
        assertEquals(STR."[\{testData.testObject2()}]", value);
    }

    @Test
    public void modifyFailure() throws IOException {
        jsonFileHandler.add(testData.testObject1(), Path.of(getFilePath()));
        try {
            jsonFileHandler.modify("incorrect name", testData.testObject2(), Path.of(getFilePath()));
        } catch (IllegalArgumentException e){
            String value = jsonFileHandler.read(Path.of(getFilePath()));
            assertEquals(STR."[\{testData.testObject1()}]", value);
        }
    }

    @Test
    public void reset() throws IOException {
        jsonFileHandler.add(testData.testObject1(), Path.of(getResetFilePath()));
        jsonFileHandler.reset((Path.of(getFilePath())), Path.of(getResetFilePath()));
        String expectedValue = jsonFileHandler.read(Path.of(getResetFilePath()));
        String value = jsonFileHandler.read(Path.of(getFilePath()));
        assertEquals(expectedValue, value);
    }
}

