package org.example.commands;


import org.example.dth.Request;
import org.example.dth.Response;
import org.example.dth.ResponseStatus;
import org.example.managers.DBManager;
import org.example.util.ConsoleColor;
import org.example.util.Printable;
import org.example.exceptions.EmptyCollectionException;

import org.example.managers.CollectionManager;

import java.sql.SQLException;

/**
 * Clear collection
 */
public class Clear extends Command {

    private CollectionManager collectionManager;
    private DBManager dbManager;

    public Clear(CollectionManager collectionManager, DBManager dbManager) {
        super("clear", ": очистить коллекцию");
        this.collectionManager = collectionManager;
        this.dbManager = dbManager;
    }

    /**
     * clears the entire collection
     *
     * @param request
     * @return
     * @throws EmptyCollectionException
     */
    @Override
    public Response execute(Request request) {

        try {
            dbManager.clearDB();
            collectionManager.clear();
            return new Response(ResponseStatus.OK, ConsoleColor.BLUE + "Элементы удалены");
        } catch (EmptyCollectionException e) {
            return new Response(ResponseStatus.ERROR, ConsoleColor.RED + "Коллекция пуста");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
