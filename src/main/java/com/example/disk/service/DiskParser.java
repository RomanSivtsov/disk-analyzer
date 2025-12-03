package com.example.disk.service;

import com.example.disk.model.DiskStat;
import java.util.List;

/**
 * Интерфейс для парсинга вывода системных команд.
 * Преобразует текстовый вывод консоли в объекты DiskStat.
 */
public interface DiskParser {

    /**
     * Парсит список строк, полученных от системной утилиты.
     * @param commandOutput список строк (вывод консоли)
     * @return список объектов DiskStat с информацией о дисках
     */
    List<DiskStat> parse(List<String> commandOutput);
}