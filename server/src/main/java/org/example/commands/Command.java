package org.example.commands;

import org.example.dth.Request;
import org.example.dth.Response;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Objects;

/**
 * Class required to work with all teams
 * And here the execute method is inherited, which is abstract and is implemented differently in each command
 */
public abstract class Command implements Executable {

    private final String name;
    private final String description;

    /**
     * The constructor is necessary to add a description and name for the command
     *
     * @param name        command name
     * @param description description of the command
     */
    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Objects.equals(name, command.name) &&
                Objects.equals(description, command.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return name + ": " + description;
    }

    @Override
    public abstract Response execute(Request request) throws SQLException;

    protected Iterator<Command> iterator() {
        return null;
    }
}