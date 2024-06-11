package org.example;

import org.example.managers.RuntimeManager;
import org.example.util.Console;
import org.example.util.RequestHandler;
import org.example.util.Server;


public class App {
    public static void main(String[] args)  {
        Console console = new Console();

        RuntimeManager runtimeManager = new RuntimeManager(console);
        RequestHandler requestHandler = new RequestHandler(runtimeManager);
        Server server = new Server(6087, requestHandler);

        server.run();
    }
}