package org.example.util;

import org.example.dth.Request;
import org.example.dth.Response;
import org.example.dth.ResponseStatus;
import org.example.managers.RuntimeManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestHandler {
    private static final Logger logger = Logger.getLogger(RequestHandler.class.getName());
    private RuntimeManager runtimeManager;

    public RequestHandler(RuntimeManager runtimeManager) {
        this.runtimeManager = runtimeManager;
    }

    public Response handle(Request request) {
        try {
            logger.log(Level.INFO, "Обработка запроса: ", request);
            Response response = runtimeManager.interactiveMode(request);
            logger.log(Level.INFO, "Запрос обработан успешно: ", response);
            return response;
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Ошибка выполнения при обработке запроса: ", new Object[]{request, e.getMessage()});
            return new Response(ResponseStatus.ERROR, "Ошибка выполнения: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Неожиданная ошибка при обработке запроса: ", new Object[]{request, e.getMessage()});
            return new Response(ResponseStatus.ERROR, "Неожиданная ошибка: " + e.getMessage());
        }
    }
}