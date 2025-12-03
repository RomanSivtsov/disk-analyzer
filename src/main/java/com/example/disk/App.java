package com.example.disk;

import com.example.disk.config.Config;
import com.example.disk.model.DiskStat;
import com.example.disk.service.CommandExecutor;
import com.example.disk.service.DiskParser;
import com.example.disk.service.impl.LinuxDiskParser;
import com.example.disk.service.impl.SystemCommandExecutor;
import com.example.disk.service.impl.WindowsDiskParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        try {
            Config config = new Config("app.properties");
            String os = System.getProperty("os.name").toLowerCase();
            CommandExecutor executor = new SystemCommandExecutor();
            DiskParser parser;
            String command;

            if (os.contains("win")) {
                parser = new WindowsDiskParser();
                command = "wmic logicaldisk get Caption,FreeSpace,Size";
            } else {
                parser = new LinuxDiskParser();
                command = "df -P -k";
            }

            List<String> output = executor.execute(command);
            List<DiskStat> disks = parser.parse(output);

            List<Map<String, Object>> exceeded = disks.stream()
                    .filter(d -> d.getUsePercent() > config.getThreshold())
                    .map(d -> {
                        Map<String, Object> map = new LinkedHashMap<>();
                        map.put("filesystem", d.getFilesystem());
                        map.put("total", format(d.getTotalBytes(), config.getUnit()));
                        map.put("used_percent", Math.round(d.getUsePercent()));
                        return map;
                    }).collect(Collectors.toList());

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