package org.example.commands;

import org.example.dth.Request;
import org.example.dth.Response;
import org.example.dth.ResponseStatus;
import org.example.managers.DBManager;
import org.example.util.ConsoleColor;
import org.example.util.Printable;
import org.example.exceptions.NoSuchId;
import org.example.managers.CollectionManager;

/**
 * update value collection element id Обновляет
 */
public class UpdateID extends Command {
    private CollectionManager collectionManager;
    private DBManager dbManager;

    public UpdateID(CollectionManager collectionManager, DBManager dbManager) {
        super("update_id", " {element}: обновить значение элемента коллекции, id которого равен заданному");
        this.collectionManager = collectionManager;
        this.dbManager = dbManager;
    }

    @Override
    public Response execute(Request request) {
        System.out.println(request.getArg());
        String[] args = request.getArg().trim().split(" ", 2);

        if (args.length != 2) {
            String txt = "Ожидается два идентификатора. Проверьте формат входных данных.";
            System.out.println(txt);
            return new Response(ResponseStatus.ERROR, ConsoleColor.RED + txt);
        }

        try {

            long id = Long.parseLong(args[0]);
            long newId = Long.parseLong(args[1]);

            if (!collectionManager.checkID(id)) throw new NoSuchId();

            collectionManager.updateId(id, newId);
            dbManager.updateID((int) id, (int) newId); // Если ваши ID действительно умещаются в int

            String txt = "Изменение id объекта Person успешно";
            System.out.println(txt);
            return new Response(ResponseStatus.OK, "Изменение id объекта Person успешно");
        } catch (NoSuchId noSuchId) {
            String txt = "Объекта с id " + args[0] + " нет в коллекции. Введите верный id, чтобы изменить объект...";
            System.out.println(txt);
            return new Response(ResponseStatus.ERROR, ConsoleColor.RED + txt);
        } catch (NumberFormatException formatException) {
            String txt = "id должно быть числом типа long";
            System.out.println(txt);
            return new Response(ResponseStatus.ERROR, ConsoleColor.RED + txt);
        }
    }


}
