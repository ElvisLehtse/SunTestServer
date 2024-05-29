package com.test;

import com.sun.net.httpserver.HttpServer;
import com.test.filehandlers.FileHandler;
import com.test.filehandlers.JsonFileHandler;
import com.test.requesthandlers.RequestHandler;

import java.io.*;
import java.net.InetSocketAddress;

import static java.lang.StringTemplate.STR;

public class SunTestServer
{
    /**
     * The main class initializes the localhost server on port 8080.
     * The server uses three endpoints and three handlers to handle all requests.
     */
    public static void main( String[] args ) {
        final int port = 8080;
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            FileHandler fileType = new JsonFileHandler();
            RequestHandler requestHandler = new RequestHandler(fileType);

            requestHandler.requestGetAndPost(server, "/Weapons");
            requestHandler.requestPutAndDelete(server, "/Weapons/ID");
            requestHandler.requestPutReset(server, "/Weapons/Reset");

            server.start();
        } catch (IOException e) {
            System.out.println(STR."\{e.getMessage()} Could not create the server");
        }
    }
}