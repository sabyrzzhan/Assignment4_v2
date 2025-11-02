package graph.common;

import graph.scc.TarjanSCC;
import java.util.*;

public class Condensation {
    public static class Result {
        public final Graph dag;
        public final int[] compId;
        public final int compCount;
        public Result(Graph dag, int[] compId, int compCount){
            this.dag=dag; this.compId=compId; this.compCount=compCount;
        }
    }
    public static Result build(Graph g, Metrics M){
        TarjanSCC scc = new TarjanSCC(g,M);
        int[] id = scc.compId();
        int C = scc.compCount();
        Graph dag = new Graph(C,true);
        Map<Long, Double> minW = new HashMap<>();
        for (var e: g.edges()){
            int cu=id[e.u], cv=id[e.v];
            if (cu!=cv){
                long key = (((long)cu)<<32) | (cv & 0xffffffffL);
                double w = minW.getOrDefault(key, Double.POSITIVE_INFINITY);
                if (e.w < w) minW.put(key, e.w);
            }
        }
        for (var entry: minW.entrySet()){
            int cu=(int)(entry.getKey()>>32);
            int cv=(int)(long)entry.getKey();
            dag.addEdge(cu, cv, entry.getValue());
        }
        return new Result(dag, id, C);
    }
}
