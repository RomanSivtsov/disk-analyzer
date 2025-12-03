package com.example.disk.service;

import java.io.IOException;
import java.util.List;

/**
 * Интерфейс для выполнения системных команд в терминале ОС.
 */
public interface CommandExecutor {
    List<String> execute(String command) throws IOException, InterruptedException;
}