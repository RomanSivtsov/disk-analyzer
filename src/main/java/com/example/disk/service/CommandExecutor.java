package com.example.disk.service;

import java.io.IOException;
import java.util.List;

public interface CommandExecutor {
    List<String> execute(String command) throws IOException, InterruptedException;
}