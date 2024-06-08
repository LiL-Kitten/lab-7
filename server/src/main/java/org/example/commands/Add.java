package org.example.commands;


import org.example.data.Person;
import org.example.dth.Request;
import org.example.dth.Response;
import org.example.dth.ResponseStatus;
import org.example.managers.DBManager;
import org.example.util.ConsoleColor;

import org.example.util.Printable;

import org.example.managers.CollectionManager;

import java.sql.SQLException;
import java.util.Objects;


/**
 * Add a new item to the collection
 */

public class Add extends Command {

    private CollectionManager collectionManager;
    private DBManager dbManager;
    private Printable console;


    public Add(CollectionManager collectionManager, DBManager dbManager, Printable console) {
        super("add", " {element} добавить новый элемент в коллекцию");
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
        this.console = console;
    }

    /**
     * performs adding a new element to the collection
     *
     * @param request An empty string is passed for this command
     */
    @Override
    public Response execute(Request request) throws SQLException {
        Person obj = request.getObj();

        // Отладочный вывод для проверки объекта
        System.out.println("Object in AddCommand: " + obj);
        System.out.println(request.getUser().getLogin() + "   " + request.getUser().getPassword());
        if (Objects.isNull(obj)) {
            return new Response(ResponseStatus.ASK_OBJECT, "Для команды add требуется объект");
        } else {
            dbManager.connect();
            boolean isAdded = dbManager.addPerson(obj, request.getUser().getLogin().trim(), request.getUser().getPassword().trim());

            if (isAdded) {
                console.println("данные успешно отправились в БД");
                collectionManager.addElement(obj);
                return new Response(ResponseStatus.OK, "Объект успешно добавлен");
            } else {
                return new Response(ResponseStatus.ERROR, "Не удалось добавить объект в БД");
            }
        }
    }

}

