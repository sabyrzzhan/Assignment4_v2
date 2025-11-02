package graph.dagsp;

import graph.common.Graph;
import graph.common.Metrics;
import graph.topo.TopologicalSort;
import java.util.*;

public class DagShortestPaths {
    private final double[] dist;
    private final int[] prev;
    public DagShortestPaths(Graph dag, int s, Metrics M){
        int n=dag.n();
        dist=new double[n]; prev=new int[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(prev, -1);
        dist[s]=0;
        var order = TopologicalSort.kahn(dag,M);
        for (int u: order){
            for (var e: dag.adj().get(u)){
                if (Double.isFinite(dist[u]) && dist[u]+e.w<dist[e.v]){
                    dist[e.v]=dist[u]+e.w; prev[e.v]=u; M.inc("dag_relax",1);
                }
            }
        }
    }
    public double[] dist(){ return dist; }
    public List<Integer> pathTo(int t){
        if (!Double.isFinite(dist[t])) return List.of();
        List<Integer> path=new ArrayList<>();
        for (int v=t; v!=-1; v=prev[v]) path.add(v);
        java.util.Collections.reverse(path);
        return path;
    }
}
