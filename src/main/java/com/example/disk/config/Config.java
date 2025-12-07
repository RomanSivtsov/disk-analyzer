package com.example.disk.config;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private final double threshold;
    private final String unit;

    public Config(String fileName) {
        Properties props = new Properties();
        if (fileName != null) {
            try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
                if (input != null) {
                    props.load(input);
                }
            } catch (Exception ignored) { }
        }
        // Значения по умолчанию, если файл не прочитан
        this.threshold = Double.parseDouble(props.getProperty("usage.threshold.percent", "90"));
        this.unit = props.getProperty("output.units", "GB");
    }

    public double getThreshold() { return threshold; }
    public String getUnit() { return unit; }
}