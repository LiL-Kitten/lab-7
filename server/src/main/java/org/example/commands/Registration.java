package org.example.commands;

import org.example.dth.Request;
import org.example.dth.Response;
import org.example.dth.ResponseStatus;
import org.example.managers.DBManager;
import org.example.util.Printable;

import java.sql.SQLException;

public class Registration extends Command {

    private Printable console;
    private DBManager manager;

    public Registration(Printable console, DBManager manager) {
        super("registration", ": проверка существования профиля и регистрация");
        this.manager = manager;
        this.console = console;
    }

    @Override
    public Response execute(Request request) throws SQLException {
        // Проверка на null для пользователя из запроса
        console.println(request.getUser().getLogin()+ "   " + request.getUser().getPassword());
        if (request.getUser() == null) {
            return new Response(ResponseStatus.CHECK_REGISTRATION,
                    "Введите данные пользователя (логин и пароль)");
        }

        // Проверка на существование логина
        boolean userExists = manager.checkLogin(request.getUser().getLogin());

        // Если пользователь не существует, предлагаем зарегистрироваться
        if (!userExists) {

            if ("log_in".equals(request.getArg())) {
                // Логика для регистрации нового пользователя
                manager.createUser(request.getUser().getLogin(), request.getUser().getPassword());
                return new Response(ResponseStatus.OK,
                        "Пользователь успешно зарегистрирован. Теперь вы можете выполнять команды.");
            } else {
                return new Response(ResponseStatus.REGISTRATION,
                        "Такого пользователя нет в БД. Пожалуйста, зарегистрируйтесь. Укажите логин и пароль для вашего будущего профиля");
            }
        } else {
            // Если логин существует, проверяем на правильность пароля
            boolean passwordIsValid = manager.checkUser(request.getUser().getLogin(), request.getUser().getPassword());

            if (!passwordIsValid) {
                return new Response(ResponseStatus.WRONG_PASSWORD,
                        "Неверно указан пароль! Введите повторно логин и пароль");
            } else {
                return new Response(ResponseStatus.OK, "Аутентификация успешна, работайте!");

            }
        }
    }
}
