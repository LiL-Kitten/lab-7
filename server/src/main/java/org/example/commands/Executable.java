package org.example.commands;

import org.example.dth.Request;
import org.example.dth.Response;

import java.sql.SQLException;

/**
 * Interface for executing any commands
 */
public interface Executable {
    public Response execute(Request request) throws SQLException;
}
