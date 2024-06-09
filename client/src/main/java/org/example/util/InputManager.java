package org.example.util;

import org.example.commands.ExecuteScript;
import org.example.commands.Exit;
import org.example.data.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public User registration() {
        try {
            String[] userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
            String login = userCommand[0];
            String pass = userCommand[1];

            // Кодирование пароля в MD5
            String md5Pass = encodeToMD5(pass);

            return new User(login, md5Pass);
        } catch (NoSuchElementException e) {
            System.out.println(ConsoleColor.RED + "Пользовательский ввод не обнаружен!");
            System.exit(1);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(ConsoleColor.RED + "Ошибка алгоритма хеширования!");
            System.exit(1);
        }
        return null;
    }

    public User changePassword(User user) {
        try {
            System.out.println("Введите новый пароль: ");
            String newPassword = scanner.nextLine().trim();

            // Кодирование нового пароля в MD5
            String md5NewPass = encodeToMD5(newPassword);

            // Создание нового пользователя с обновленным паролем
            user.setPassword(md5NewPass);
            return user;
        } catch (NoSuchElementException e) {
            System.out.println(ConsoleColor.RED + "Пользовательский ввод не обнаружен!");
            System.exit(1);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(ConsoleColor.RED + "Ошибка алгоритма хеширования!");
            System.exit(1);
        }
        return null;
    }



    private String encodeToMD5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : messageDigest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
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
