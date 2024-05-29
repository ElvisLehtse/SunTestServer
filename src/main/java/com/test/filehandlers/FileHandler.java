package com.test.filehandlers;

import java.io.IOException;
import java.nio.file.Path;

public interface FileHandler {

    String read(Path filePath) throws IOException;
    void add(String requestBody, Path filePath) throws IOException;
    void modify(String oldName, String requestBody, Path filePath) throws IOException;
    void delete(String weaponsName, Path filePath) throws IOException;
    void reset(Path filePath, Path resetFilePath) throws IOException;

}
