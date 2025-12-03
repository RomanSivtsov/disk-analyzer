package com.example.disk;

import com.example.disk.model.DiskStat;
import com.example.disk.service.impl.LinuxDiskParser;
import com.example.disk.service.impl.WindowsDiskParser;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    @Test
    void testLinuxParser() {
        List<String> mock = Arrays.asList(
                "Filesystem     1024-blocks      Used Available Capacity Mounted on",
                "/dev/sda1        10485760   5242880   5242880      50% /"
        );
        LinuxDiskParser parser = new LinuxDiskParser();
        List<DiskStat> res = parser.parse(mock);
        assertEquals(1, res.size());
        assertEquals(10737418240L, res.get(0).getTotalBytes());
    }

    @Test
    void testWindowsParser() {
        List<String> mock = Arrays.asList(
                "Caption  FreeSpace    Size",
                "C:       50000000000  100000000000"
        );
        WindowsDiskParser parser = new WindowsDiskParser();
        List<DiskStat> res = parser.parse(mock);
        assertEquals(1, res.size());
        assertEquals(50.0, res.get(0).getUsePercent(), 0.1);
    }
}