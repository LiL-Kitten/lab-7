package org.example.managers;

import org.example.data.*;
import org.example.dth.Request;
import org.example.dth.Response;
import org.example.dth.ResponseStatus;
import org.example.exceptions.CommandRuntimeError;
import org.example.exceptions.ExitObliged;
import org.example.exceptions.NoSuchCommand;
import org.example.util.Printable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * a class for working with all available commands, passing arguments to them
 */
public class RuntimeManager {
    private Printable console;
    private CommandManager commandManager;

    private DBManager dbManager;
    private CollectionManager collectionManager;

    public RuntimeManager(Printable console) {
        this.console = console;
        this.collectionManager = new CollectionManager();
        this.dbManager = new DBManager(console, collectionManager);
        this.commandManager = new CommandManager(collectionManager, dbManager);
        dbManager.connect();
        dbManager.pushInCollection();
        console.println("Коллекция инициализирована!");

        console.println(collectionManager.getTotalID() + " текущий счетчик  ID-шников персонов в коллекции (а может и нет я хз)");

        System.out.println("RuntimeManager инициализирован");
    }

    public Response interactiveMode(Request request) throws RuntimeException {
        try {
            System.out.println("interactiveMode: Начало выполнения");

            commandManager.addToHistory(request.getCommand());

            System.out.println("interactiveMode: До вызова launch для команды: " + request.getCommand());
            Response response = this.launch(request);

            System.out.println("interactiveMode: После вызова launch");
            return response;
        } catch (NoSuchElementException exception) {
            console.printError("Пользовательский ввод не обнаружен!");
            return new Response(ResponseStatus.ERROR, "Пользовательский ввод не обнаружен!");
        } catch (NoSuchCommand noSuchCommand) {
            console.printError("Такой команды нет в списке");
            console.println("Введите help для того чтобы вывести список команд");
            return new Response(ResponseStatus.ERROR, "Такой команды нет в списке");
        } catch (CommandRuntimeError e) {
            console.printError("Ошибка при исполнении команды");
            return new Response(ResponseStatus.ERROR, "Ошибка при исполнении команды");
        } catch (ExitObliged exitObliged) {
            return new Response(ResponseStatus.ERROR, "выходим");
        } catch (IOException e) {
            return new Response(ResponseStatus.ERROR, "ошибка");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("interactiveMode: Конец выполнения");
        }
    }

    public Response launch(Request request) throws IOException, ExitObliged, IllegalArgumentException, SQLException {
        System.out.println("launch: Выполнение команды: " + request.getCommand());
        Response response = commandManager.execute(request);
        System.out.println("launch: Получен ответ для команды: " + request.getCommand());
        return response;
    }
}