package strmatch.util;

public class Timer {
    private long startTime;
    private long endTime;

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        endTime = System.nanoTime();
    }

    public long getDurationNano() {
        return endTime - startTime;
    }

    public long getDurationMillis() {
        return (endTime - startTime) / 1_000_000; // 纳秒转换为毫秒
    }

    public double getDurationSeconds() {
        return (endTime - startTime) / 1_000_000_000.0; // 纳秒转换为秒
    }
}

