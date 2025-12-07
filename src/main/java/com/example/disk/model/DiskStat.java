package com.example.disk.model;

public class DiskStat {
    private final String filesystem;
    private final long totalBytes;
    private final long usedBytes;
    private final long freeBytes;
    private final double usePercent;

    public DiskStat(String filesystem, long totalBytes, long usedBytes) {
        this.filesystem = filesystem;
        this.totalBytes = totalBytes;
        this.usedBytes = usedBytes;
        this.freeBytes = totalBytes - usedBytes;
        this.usePercent = totalBytes == 0 ? 0 : ((double) usedBytes / totalBytes) * 100.0;
    }

    public String getFilesystem() { return filesystem; }
    public long getTotalBytes() { return totalBytes; }
    public long getUsedBytes() { return usedBytes; }
    public long getFreeBytes() { return freeBytes; }
    public double getUsePercent() { return usePercent; }
}