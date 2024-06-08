package org.example.dth;

import org.example.data.Person;
import org.example.data.User;

import java.io.Serializable;

public class Request implements Serializable
{
    private static final long serialVersionUID = 8877794645528610544L;
    private String command;
    private String arg;
    private Person person;

    private User user;

    public Request(Person person, User user) {
        this.person = person;
        this.user = user;
    }

    public Request(String command, String arg, User user) {
        this.command = command.trim();
        this.arg = arg.trim();
        this.user = user;

    }

    public Request(String command, Person person, User user) {
        this.command = command.trim();
        this.person = person;
        this.user = user;
    }

    public Request(String command, User user) {
        this.user = user;
        this.command = command.trim();
    }

    public Request(String command) {
        this.command = command.trim();
    }

    public  String getCommand() {
        return command;
    }

    public String getArg() {
        return arg;
    }

    public User getUser() {
        return user;
    }


    public Person getObj() {
        return person;
    }

    public boolean isEmpty() {
        return command.isEmpty() && arg.isEmpty() && person == null;
    }
}



//принцип следующий мы должны отправлять команды, с какими то аргументами и эти команды должны быть читаемы
// для сервера, то есть по факту идет разбиение ввода данных на массив строк, который будет проходить определенные
// проверки. Должны быть проверки самой команды на соответствие командам сервера, правильно ли переданы аргументы
// команды. Нужен статус отправки команды
//Могут быть слабые клиенты: все ресурсоемкие задачи можно перенести на сервер.
//Независимое развитие кода клиентов и кода сервера.
