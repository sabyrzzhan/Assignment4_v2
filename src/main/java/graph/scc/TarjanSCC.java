package graph.scc;

import graph.common.Graph;
import graph.common.Metrics;
import java.util.*;

public class TarjanSCC {
    private final Graph g;
    private final Metrics M;
    private int time=0, compCnt=0;
    private final int[] disc, low, compId;
    private final boolean[] inStack;
    private final Deque<Integer> st = new ArrayDeque<>();

    public TarjanSCC(Graph g, Metrics metrics){
        this.g=g; this.M=metrics;
        int n=g.n();
        disc=new int[n]; low=new int[n]; compId=new int[n]; inStack=new boolean[n];
        Arrays.fill(disc,-1); Arrays.fill(low,-1); Arrays.fill(compId,-1);
        for (int i=0;i<n;i++) if (disc[i]==-1) dfs(i);
    }
    private void dfs(int u){
        M.inc("scc_dfs_calls",1);
        disc[u]=low[u]=time++;
        st.push(u); inStack[u]=true;
        for (var e: g.adj().get(u)){
            M.inc("scc_edges_seen",1);
            int v=e.v;
            if (disc[v]==-1){
                dfs(v); low[u]=Math.min(low[u], low[v]);
            } else if (inStack[v]){
                low[u]=Math.min(low[u], disc[v]);
            }
        }
        if (low[u]==disc[u]){
            while(true){
                int v=st.pop(); inStack[v]=false; compId[v]=compCnt;
                if (v==u) break;
            }
            compCnt++;
        }
    }
    public int[] compId(){ return compId; }
    public int compCount(){ return compCnt; }
    public List<List<Integer>> components(){
        List<List<Integer>> res=new ArrayList<>();
        for (int i=0;i<compCnt;i++) res.add(new ArrayList<>());
        for (int v=0; v<g.n(); v++) res.get(compId[v]).add(v);
        return res;
    }
}
