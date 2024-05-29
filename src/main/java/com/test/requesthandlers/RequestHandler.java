package com.test.requesthandlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.test.filehandlers.FileHandler;

import java.io.*;
import java.nio.file.Path;

import static java.lang.StringTemplate.STR;

public class RequestHandler {

    /**
     * This class handles the requests. It reads the request method and calls the
     * corresponding action in the JsonParser class.
     * For PUT and DELETE requests, it reads the query parameter value
     * and uses it to match the name of the JSON object.
     * If multiple objects have the same name, action is taken towards
     * all objects of that name. Finally, it returns a reply message
     * back to the client.
     */

    private final FileHandler fileHandler;
    private final Path filePath = Path.of("text.json");

    public RequestHandler (FileHandler fileType) {
        this.fileHandler = fileType;
    }

    private BufferedReader requestBodyMsg (HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }

    private void serverResponse (HttpExchange exchange, String reply) {
        try {
            exchange.sendResponseHeaders(200, reply.length());
            OutputStream output = exchange.getResponseBody();
            output.write(reply.getBytes());
            output.flush();
            output.close();
            exchange.close();
        } catch (IOException e) {
            System.out.println(STR."\{e.getMessage()} Could not send reply to client");
        }
    }

    public void requestGetAndPost (HttpServer server, String requestPath) {

        server.createContext(requestPath, (HttpExchange exchange) ->
        {
            String reply = "";
            try {
                if (exchange.getRequestMethod().equals("GET")) {
                    reply = fileHandler.read(filePath);
                } else if (exchange.getRequestMethod().equals("POST")) {
                    BufferedReader requestBody = requestBodyMsg(exchange);
                    fileHandler.add(requestBody.readLine(), filePath);
                    reply = "New weapon added";
                }
            } catch (IOException e) {
                System.out.println(STR."\{e.getMessage()} Could not access the file");
                reply = "Could not access the file";
            } finally {
                serverResponse(exchange, reply);
            }
        });
    }

    public void requestPutAndDelete (HttpServer server, String requestPath) {

        server.createContext(requestPath, (HttpExchange exchange) ->
        {
            String reply = "";
            String weaponsName = exchange.getRequestURI().getQuery().replace("ID=", "");
            try {
                if (exchange.getRequestMethod().equals("PUT")) {
                    BufferedReader requestBody = requestBodyMsg(exchange);
                    fileHandler.modify(weaponsName, requestBody.readLine(), filePath);
                    reply = (STR."\{weaponsName} has been modified");

                } else if (exchange.getRequestMethod().equals("DELETE")) {
                    fileHandler.delete(weaponsName, filePath);
                    reply = (STR."\{weaponsName} has been deleted");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + STR."Could not find a weapon labeled \{weaponsName}");
                reply = (STR."Could not find a weapon labeled \{weaponsName}");
            } catch (IOException e) {
                System.out.println(STR."\{e.getMessage()} Could not access the file");
                reply = "Could not access the file";
            } finally {
                serverResponse(exchange, reply);
            }
        });
    }

    public void requestPutReset (HttpServer server, String requestPath) {

        server.createContext(requestPath, (HttpExchange exchange) ->
        {
            Path resetFilePath = Path.of("reset.json");
            String reply = "";
            try {
                fileHandler.reset(filePath, resetFilePath);
                reply = "File has been reset";
            } catch (IOException e) {
                System.out.println(STR."\{e.getMessage()} Could not access the file");
                reply = "Could not access the file";
            } finally {
                serverResponse(exchange, reply);
            }
        });
    }
}
