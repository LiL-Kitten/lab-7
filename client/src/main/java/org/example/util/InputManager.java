package org.example.util;

import org.example.commands.ExecuteScript;
import org.example.commands.Exit;
import org.example.data.User;

import java.util.NoSuchElementException;
import java.util.Scanner;


public class InputManager {

    private Scanner scanner = new Scanner(System.in);
    private String command;
    private String argument;
    private User user;

    public InputManager() {
    }

    public void commandInput() {
        try {
            String[] userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
            command = userCommand[0];
            argument = userCommand[1];
        } catch (NoSuchElementException exception) {
            System.out.println(ConsoleColor.RED +"Пользовательский ввод не обнаружен!");
            System.exit(1);
        }
    }

    public User registration(){
        try {
            String[] userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
            return user = new User(userCommand[0],userCommand[1]);
        } catch (NoSuchElementException e) {
            System.out.println(ConsoleColor.RED +"Пользовательский ввод не обнаружен!");
            System.exit(1);
        }
        return null;
    }

    public String getCommand() {
        return command.trim();
    }
    public String getArgument() {
        return argument.trim();
    }

    public User getUser() {
        return user;
    }
}
