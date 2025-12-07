package com.example.disk.service.service.impl;

import com.example.disk.model.DiskStat;
import com.example.disk.service.DiskParser;
import java.util.ArrayList;
import java.util.List;

public class LinuxDiskParser implements DiskParser {
    @Override
    public List<DiskStat> parse(List<String> output) {
        List<DiskStat> stats = new ArrayList<>();
        for (int i = 1; i < output.size(); i++) {
            String line = output.get(i).trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            if (parts.length < 6) continue;

            try {
                String fs = parts[0];
                long totalKb = Long.parseLong(parts[1]);
                long usedKb = Long.parseLong(parts[2]);
                stats.add(new DiskStat(fs, totalKb * 1024, usedKb * 1024));
            } catch (NumberFormatException ignored) { }
        }
        return stats;
    }
}