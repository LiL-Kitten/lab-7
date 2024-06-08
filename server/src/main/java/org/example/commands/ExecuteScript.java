package org.example.commands;

import org.example.dth.Request;
import org.example.dth.Response;
import org.example.dth.ResponseStatus;
import org.example.util.Printable;

import org.example.managers.CommandManager;


/**
 * One of the most steamy commands, necessary to read and execute a script from the specified file
 * The script contains commands in the same form in which the user enters them in interactive mode.
 */
public class ExecuteScript extends Command {
    private Printable console;
    private CommandManager commandManager;


    public ExecuteScript(Printable console, CommandManager commandManager) {
        super("execute_script", ": считать и исполнить скрипт из указанного файла");
        this.console = console;
        this.commandManager = commandManager;
    }

    @Override
    public Response execute(Request request) {
        return  new Response(ResponseStatus.OK,"Рекурсия скрипта выполнялась");
    }

}
