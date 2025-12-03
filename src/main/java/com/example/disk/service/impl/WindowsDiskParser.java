package com.example.disk.service.impl;

import com.example.disk.model.DiskStat;
import com.example.disk.service.DiskParser;
import java.util.ArrayList;
import java.util.List;

public class WindowsDiskParser implements DiskParser {
    @Override
    public List<DiskStat> parse(List<String> output) {
        List<DiskStat> stats = new ArrayList<>();
        for (int i = 1; i < output.size(); i++) {
            String line = output.get(i).trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            if (parts.length < 3) continue;

            try {
                // wmic output: Caption FreeSpace Size
                String fs = parts[0];
                long freeBytes = Long.parseLong(parts[1]);
                long totalBytes = Long.parseLong(parts[2]);
                long usedBytes = totalBytes - freeBytes;
                stats.add(new DiskStat(fs, totalBytes, usedBytes));
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        return stats;
    }
}