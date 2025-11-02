package graph.common;

public class Stopwatch implements AutoCloseable {
    private final String label;
    private final long start;
    public final long[] elapsedNs = new long[1];
    public Stopwatch(String label){
        this.label = label;
        this.start = System.nanoTime();
    }
    public void close(){
        elapsedNs[0] = System.nanoTime() - start;
        System.out.printf("[TIME] %s: %,d ns (%.3f ms)%n", label, elapsedNs[0], elapsedNs[0]/1e6);
    }
}
