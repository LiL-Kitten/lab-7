package org.example.commands;

import org.example.dth.Request;
import org.example.util.*;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;


public class ExecuteScript extends Command {
    private Printable console;
    private Client client;
    private RuntimeManager run;
    private ExecuteFileManager executeFileManager;

    public ExecuteScript(Printable console, Client client, RuntimeManager run) {
        super("execute_script", "выполнение скрипта");
        this.console = console;
        this.client = client;
        this.run = run;
    }

    @Override
    public void execute(String args) {
        console.println("процесс выполнения начался");
        try {
            executeFileManager = new ExecuteFileManager(Path.of(args));
        } catch (FileNotFoundException e) {
            console.println("Файл не найден: " + e.getMessage());
            return;
        } catch (IOException e) {
            console.println("Ошибка чтения файла: " + e.getMessage());
            return;
        }

        while (executeFileManager.hasElements()) {
            run.processResponse( client.sendAndAskResponse(new Request(executeFileManager.getNextElement())));

        }
    }
}

class ExecuteFileManager {
    private Deque<String> lines = new ArrayDeque<>();

    private final Path filePath;

    public ExecuteFileManager(Path path) throws IOException {
        this.filePath = path;


        File file = filePath.toFile();


        if (!file.exists()) {
            System.out.println(path.toFile().getAbsolutePath());

            throw new FileNotFoundException("Файл не существует: " + filePath);
        }


        if (!file.canRead()) {
            throw new IOException("Файл недоступен для чтения: " + filePath);
        }


        readAndProcessFile(file);
    }

    private void readAndProcessFile(File file) throws IOException {
        System.out.println(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    if (!word.trim().isEmpty()) {
                        lines.add(word.trim());
                    }
                }
            }
        }
    }

    public String getNextElement() {
        return lines.poll();
    }

    public boolean hasElements() {
        return !lines.isEmpty();
    }
}
