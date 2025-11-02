package graph.common;

import java.util.concurrent.ConcurrentHashMap;

public class SimpleMetrics implements Metrics {
    private final ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
    public void inc(String key, long delta){ map.merge(key, delta, Long::sum); }
    public long get(String key){ return map.getOrDefault(key,0L); }
    public String toString(){ return map.toString(); }
}
