package com.example.disk.service;

import com.example.disk.model.DiskStat;
import java.util.List;

public interface DiskParser {
    List<DiskStat> parse(List<String> commandOutput);
}