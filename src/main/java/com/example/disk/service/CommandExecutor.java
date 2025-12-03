package com.example.disk.service;

import java.io.IOException;
import java.util.List;

/**
 * Интерфейс для выполнения системных команд в терминале ОС.
 */
public interface CommandExecutor {

    /**
     * Выполняет переданную команду и возвращает результат выполнения.
     * @param command строка команды (например, "df -h")
     * @return список строк, которые команда вывела в консоль
     * @throws IOException если произошла ошибка ввода-вывода
     * @throws InterruptedException если поток был прерван во время ожидания
     */
    List<String> execute(String command) throws IOException, InterruptedException;
}