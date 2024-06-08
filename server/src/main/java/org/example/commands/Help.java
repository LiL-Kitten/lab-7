package org.example.commands;

import org.example.dth.Request;
import org.example.dth.Response;
import org.example.dth.ResponseStatus;
import org.example.util.ConsoleColor;
import org.example.util.Printable;
import org.example.managers.CollectionManager;
import org.example.managers.CommandManager;

import java.util.stream.Collectors;

/**
 * displays help on available commands
 */
public class Help extends Command {

    private CollectionManager collectionManager;
    private CommandManager commandManager;

    public Help(CommandManager commandManager) {
        super("help", ": Вывести справку по командам");
        this.commandManager = commandManager;
    }

    @Override
    public Response execute(Request request) {
        String commandList = commandManager.getCommands().stream()
                .map(command -> ConsoleColor.toColor(command.getName(), ConsoleColor.GREEN) + command.getDescription())
                .collect(Collectors.joining("\n"));
        return new Response(ResponseStatus.OK, commandList);
    }
}
