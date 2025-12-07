package com.example.disk;

import com.example.disk.config.Config;
import com.example.disk.model.DiskStat;
import com.example.disk.service.CommandExecutor;
import com.example.disk.service.DiskParser;
import com.example.disk.service.service.impl.LinuxDiskParser;
import com.example.disk.service.service.impl.WindowsDiskParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.disk.service.impl.SystemCommandExecutor;

/**
 * Главный класс приложения.
 */
public class App {
    public static void main(String[] args) {
        try {
            // Загружаем конфиг, если файла нет - используем дефолт
            Config config;
            try {
                config = new Config("app.properties");
            } catch (Exception e) {
                System.err.println("Warning: app.properties not found, using defaults.");
                config = new Config(null); // Конструктор обработает null
            }

            String os = System.getProperty("os.name").toLowerCase();
            CommandExecutor executor = new SystemCommandExecutor();
            DiskParser parser;
            String command;

            // Определяем ОС и команду
            if (os.contains("win")) {
                parser = new WindowsDiskParser();
                // Используем PowerShell для Windows 10/11
                command = "powershell -Command \"Get-CimInstance -ClassName Win32_LogicalDisk | Select-Object DeviceID,FreeSpace,Size | Format-Table -HideTableHeaders\"";
            } else {
                parser = new LinuxDiskParser();
                command = "df -P -k";
            }

            List<String> output = executor.execute(command);
            List<DiskStat> disks = parser.parse(output);

            // Фильтрация
            Config finalConfig = config;
            List<Map<String, Object>> exceeded = disks.stream()
                    .filter(d -> d.getUsePercent() > finalConfig.getThreshold())
                    .map(d -> {
                        Map<String, Object> map = new LinkedHashMap<>();
                        map.put("filesystem", d.getFilesystem());
                        map.put("total", format(d.getTotalBytes(), finalConfig.getUnit()));
                        map.put("used_percent", Math.round(d.getUsePercent()));
                        return map;
                    }).collect(Collectors.toList());

            // Агрегация
            long total = disks.stream().mapToLong(DiskStat::getTotalBytes).sum();
            long used = disks.stream().mapToLong(DiskStat::getUsedBytes).sum();

            Map<String, Object> summary = new LinkedHashMap<>();
            String unit = config.getUnit();
            summary.put("total_" + unit.toLowerCase(), formatRaw(total, unit));
            summary.put("total_used_" + unit.toLowerCase(), formatRaw(used, unit));

            Map<String, Object> report = new LinkedHashMap<>();
            report.put("threshold_exceeded", exceeded);
            report.put("summary", summary);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(report));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String format(long bytes, String unit) {
        return formatRaw(bytes, unit) + unit;
    }

    private static long formatRaw(long bytes, String unit) {
        double div = unit.equalsIgnoreCase("GB") ? (1024.0 * 1024 * 1024) : (1024.0 * 1024);
        return Math.round(bytes / div);
    }
}