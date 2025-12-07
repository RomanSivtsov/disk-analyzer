package com.example.disk;

import com.example.disk.model.DiskStat;
import com.example.disk.service.service.impl.WindowsDiskParser;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    @Test
    void testWindowsParser() {
        List<String> mock = Arrays.asList("C: 500 1000");
        WindowsDiskParser parser = new WindowsDiskParser();
        List<DiskStat> res = parser.parse(mock);
        assertEquals(1, res.size());
        assertEquals(1000L, res.get(0).getTotalBytes());
    }
}