package com.example.disk.service;

import com.example.disk.model.DiskStat;
import java.util.List;

public interface DiskParser {

    /**
     * Парсит список строк, полученных от системной утилиты.
     * @param commandOutput список строк (вывод консоли)
     * @return список объектов DiskStat с информацией о дисках
     */
    List<DiskStat> parse(List<String> commandOutput);
}