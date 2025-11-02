package graph.dagsp;

import graph.common.Graph;
import graph.common.Metrics;
import graph.topo.TopologicalSort;
import java.util.*;

public class DagLongestPath {
    private final double[] best;
    private final int[] prev;
    public DagLongestPath(Graph dag, Metrics M){
        int n=dag.n();
        best=new double[n]; prev=new int[n];
        Arrays.fill(best, Double.NEGATIVE_INFINITY);
        Arrays.fill(prev, -1);
        var order = TopologicalSort.kahn(dag,M);
        int[] indeg = new int[n];
        for (int u=0;u<n;u++) for (var e: dag.adj().get(u)) indeg[e.v]++;
        for (int i=0;i<n;i++) if (indeg[i]==0) best[i]=0;
        for (int u: order){
            for (var e: dag.adj().get(u)){
                if (best[u]+e.w > best[e.v]){
                    best[e.v]=best[u]+e.w; prev[e.v]=u; M.inc("dag_long_relax",1);
                }
            }
        }
    }
    public double[] best(){ return best; }
    public int argMax(){
        int idx=0; for (int i=1;i<best.length;i++) if (best[i]>best[idx]) idx=i; return idx;
    }
    public List<Integer> pathTo(int t){
        if (!Double.isFinite(best[t])) return List.of();
        List<Integer> path=new ArrayList<>();
        for (int v=t; v!=-1; v=prev[v]) path.add(v);
        java.util.Collections.reverse(path);
        return path;
    }
}
