package graph.common;

import java.util.*;

/** Immutable directed weighted graph (edge-weight model; nodeWeight reserved). */
public class Graph {
    public static class Edge {
        public final int u, v;
        public final double w;
        public Edge(int u, int v, double w){ this.u=u; this.v=v; this.w=w; }
        public String toString(){ return u+"->"+v+"("+w+")"; }
    }
    private final int n;
    private final boolean directed;
    private final List<List<Edge>> adj;
    private final double[] nodeWeight;
    public Graph(int n, boolean directed){
        this.n=n; this.directed=directed;
        this.adj = new ArrayList<>(n);
        for(int i=0;i<n;i++) adj.add(new ArrayList<>());
        this.nodeWeight = new double[n];
    }
    public int n(){ return n; }
    public boolean directed(){ return directed; }
    public List<List<Edge>> adj(){ return adj; }
    public double[] nodeWeight(){ return nodeWeight; }
    public void addEdge(int u,int v,double w){
        adj.get(u).add(new Edge(u,v,w));
        if(!directed) adj.get(v).add(new Edge(v,u,w));
    }
    public Iterable<Edge> edges(){
        List<Edge> all=new ArrayList<>();
        for(var lst: adj) all.addAll(lst);
        return all;
    }
}
