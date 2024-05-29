package com.test;

import java.io.IOException;

public interface FileHandler {

    String read() throws IOException;
    void add(String requestBody) throws IOException;
    void modify(String oldName, String requestBody) throws IOException;
    void delete(String weaponsName) throws IOException;
    void reset() throws IOException;

}
