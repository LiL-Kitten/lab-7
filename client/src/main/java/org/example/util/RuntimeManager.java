package org.example.util;

import org.example.commands.ExecuteScript;
import org.example.commands.Exit;
import org.example.data.Person;
import org.example.dth.Request;
import org.example.dth.Response;
import org.example.dth.ResponseStatus;
import org.example.forms.PersonForm;

public class RuntimeManager {
    private static Printable console;
    private InputManager inputManager;
    private Client client;
    private Exit exit;
    private ExecuteScript executeScript;
    private Response response;


    public RuntimeManager(Printable console, Client client, InputManager inputManager) {
        RuntimeManager.console = console;
        this.client = client;
        this.inputManager = inputManager;
        this.exit = new Exit(console, client, this);
        this.executeScript = new ExecuteScript(console, client, this);

    }

    public void run() {
        client.createSocket();
        console.println("введите логин и пароль от вашего профиля в виде " + ConsoleColor.BLUE + "<логин пароль>" + ConsoleColor.RESET);
        // Цикл для регистрации, повторяется до получения ответа со статусом OK
        while (true) {
            try {
                // Отправка запроса на регистрацию и обработка ответа
                Response registrationResponse = client.sendAndAskResponse(new Request("registration", inputManager.registration()));
                if (processResponse(registrationResponse) == ResponseStatus.OK) {
                    break;
                }
            } catch (Exception e) {
                console.printError("Ошибка при регистрации: " + e.getMessage());
            }
        }

        // Основной цикл выполнения команд после успешной регистрации
        while (true) {
            try {
                inputManager.commandInput();

                if (inputManager.getCommand().equalsIgnoreCase("exit")) {
                    exit.execute(inputManager.getArgument());
                } else if (inputManager.getCommand().equalsIgnoreCase("execute_script")) {
                    executeScript.execute(inputManager.getArgument());
                    continue;
                } else {
                    try {
                        response = client.sendAndAskResponse(new Request(inputManager.getCommand(), inputManager.getArgument(), inputManager.getUser()));
                    } catch (Exception e) {
                        console.printError("Ошибка при отправке запроса: " + e.getMessage());
                        break;
                    }
                }
                processResponse(response);

            } catch (Exception e) {
                console.printError("Произошла ошибка: " + e.getMessage());
            }
        }
    }

    public ResponseStatus processResponse(Response response) {
        if (response != null) {
            this.printResponse(response);

            switch (response.getStatus()) {
                case ASK_OBJECT:
                    Response newResponse = client.sendAndAskResponse(new Request("add", new PersonForm(console).build(), inputManager.getUser()));
                    return processResponse(newResponse);
                case OK:
                    // Ответ статус OK, значит регистрация или команда выполнена успешно
                    return ResponseStatus.OK;
                case CHECK_REGISTRATION:
                    newResponse = client.sendAndAskResponse(new Request("registration", inputManager.registration()));
                    return processResponse(newResponse);
                case WRONG_PASSWORD:
                    inputManager.registration();
                    newResponse = client.sendAndAskResponse(new Request("registration", inputManager.getUser()));
                    return processResponse(newResponse);
                case REGISTRATION:
                    inputManager.registration();
                    newResponse = client.sendAndAskResponse(new Request("log_in", inputManager.getUser()));
                    return processResponse(newResponse);

                case ERROR:
                    console.printError(response.getResponse());
                    break;
                case EXIT:
                    console.println(response.getResponse());
                    System.exit(0); // Завершаем программу
                    break;
                default:
                    console.printError("Неизвестный статус ответа: " + response.getStatus());
            }
        } else {
            console.printError("Не удалось получить ответ от сервера.");
            return ResponseStatus.ERROR;
        }
        return response != null ? response.getStatus() : ResponseStatus.ERROR;
    }

    public ResponseStatus printResponse(Response response) {
        if (response == null) {
            console.printError("Не удалось получить ответ от сервера.");
            return ResponseStatus.ERROR;
        }
        console.println(ConsoleColor.BLUE + "Получен ответ с статусом: " + ConsoleColor.RESET + response.getStatus());
        console.println(ConsoleColor.BLUE + "Сообщение: \n" + ConsoleColor.RESET + response.getResponse());
        return response.getStatus();
    }
}