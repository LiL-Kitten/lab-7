package org.example.commands;

import org.example.dth.Request;
import org.example.dth.Response;
import org.example.dth.ResponseStatus;
import org.example.managers.DBManager;
import org.example.util.Printable;

import java.sql.SQLException;

public class LogIN extends Command {
    private DBManager dbManager;
    private Printable console;

    public LogIN(Printable console, DBManager dbManager) {
        super("log_in", "регистрация в случае отсутствия профиля");
        this.console = console;
        this.dbManager = dbManager;
    }

    @Override
    public Response execute(Request request) throws SQLException {
        System.out.println(request.getUser().getLogin() +"   "+ request.getUser().getPassword());
        if(dbManager.checkUser(request.getUser().getLogin(), request.getUser().getPassword())){
         return new Response(ResponseStatus.OK, "вы уже прошли регистрацию ранее, вам зачем новый профиль?");
        } else {
            dbManager.createUser(request.getUser().getLogin(), request.getUser().getPassword());
            return new Response(ResponseStatus.OK, "регистрация прошла успешно, продолжайте работать");
        }
    }
}
