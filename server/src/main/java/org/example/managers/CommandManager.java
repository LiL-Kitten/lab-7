package org.example.managers;

import org.example.commands.*;
import org.example.dth.Request;
import org.example.dth.Response;
import org.example.dth.ResponseStatus;
import org.example.exceptions.ExitObliged;
import org.example.exceptions.NoSuchCommand;
import org.example.util.Console;
import org.example.util.Printable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * class for manipulation by collection Command with our different command
 */
public class CommandManager {

    Console console = new Console();
    private final List<String> commandHistory = new ArrayList<>();


    private final HashMap<String, Command> commands = new HashMap<>();


    public CommandManager(CollectionManager collectionManager, DBManager dbManager) {
        addCommand(new Exit( collectionManager));
        addCommand(new ExecuteScript(console, this));
        addCommand(new Add(collectionManager, dbManager, console));
        addCommand(new Help(this));
        addCommand(new Show(collectionManager));
        addCommand(new RemoveHead(collectionManager, dbManager));
        addCommand(new Registration(console, dbManager));
        addCommand(new LogIN(console, dbManager));
        addCommand(new RemoveByID(collectionManager));
        addCommand(new RemoveElements(collectionManager));
        addCommand(new PrintPassportID(collectionManager));
        addCommand(new UpdateID(collectionManager, dbManager));
        addCommand(new History(this));
        addCommand(new GroupByDate(collectionManager));
        addCommand(new FilterByName(collectionManager));
        addCommand(new Info(collectionManager));
        addCommand(new Clear(collectionManager, dbManager));
    }

    public void addCommand(Command command) {
        this.commands.put(command.getName(), command);
    }

    public Collection<Command> getCommands() {
        return this.commands.values();
    }

    public Response execute(Request request) throws IOException, ExitObliged, IllegalArgumentException, SQLException {
        System.out.println("execute: Начало выполнения для команды: " + request.getCommand());

        Command command = this.commands.get(request.getCommand());

        if (command == null) {
            System.out.println("execute: Команда не найдена: " + request.getCommand());
            return new Response(ResponseStatus.ERROR, "такой команды нет!");
        } else {
            console.println("Исполняемая команда: " + request.getCommand());
            Response response = command.execute(request);
            console.println("Полученный ответ: " + response);
            System.out.println("execute: Конец выполнения для команды: " + request.getCommand());
            return response;
        }
    }


    public void addToHistory(String line) {
        if (line.isBlank()) return;
        this.commandHistory.add(line);
    }

    public List<String> getCommandHistory() {
        return commandHistory;
    }


}


