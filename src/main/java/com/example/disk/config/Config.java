package com.example.disk.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private final double threshold;
    private final String unit;

    public Config(String fileName) throws IOException {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IOException("Unable to find " + fileName);
            }
            props.load(input);
        }
        this.threshold = Double.parseDouble(props.getProperty("usage.threshold.percent", "90"));
        this.unit = props.getProperty("output.units", "GB");
    }

    public double getThreshold() { return threshold; }
    public String getUnit() { return unit; }
}