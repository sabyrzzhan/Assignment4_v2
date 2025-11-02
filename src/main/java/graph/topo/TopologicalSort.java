package graph.topo;

import graph.common.Graph;
import graph.common.Metrics;
import java.util.*;

public class TopologicalSort {
    public static List<Integer> kahn(Graph g, Metrics M){
        int n=g.n();
        int[] indeg = new int[n];
        for (int u=0;u<n;u++) for (var e: g.adj().get(u)) indeg[e.v]++;
        Deque<Integer> q = new ArrayDeque<>();
        for (int i=0;i<n;i++) if (indeg[i]==0){ q.add(i); M.inc("kahn_pushes",1); }
        List<Integer> order=new ArrayList<>();
        while(!q.isEmpty()){
            int u=q.removeFirst(); M.inc("kahn_pops",1);
            order.add(u);
            for (var e: g.adj().get(u)){
                if (--indeg[e.v]==0){ q.addLast(e.v); M.inc("kahn_pushes",1); }
            }
        }
        if (order.size()!=n) throw new IllegalStateException("Graph is not a DAG.");
        return order;
    }
}
