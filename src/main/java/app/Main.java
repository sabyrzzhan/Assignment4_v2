package app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import graph.common.*;
import graph.dagsp.*;
import graph.topo.TopologicalSort;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static Graph readJson(String path) throws Exception {
        ObjectMapper om = new ObjectMapper();
        JsonNode root = om.readTree(new File(path));
        boolean directed = root.get("directed").asBoolean();
        int n = root.get("n").asInt();
        Graph g = new Graph(n, directed);
        for (JsonNode e: root.get("edges")){
            int u = e.get("u").asInt();
            int v = e.get("v").asInt();
            double w = e.has("w") ? e.get("w").asDouble() : 1.0;
            g.addEdge(u,v,w);
        }
        return g;
    }
    public static int readSource(String path) throws Exception {
        ObjectMapper om = new ObjectMapper();
        JsonNode root = om.readTree(new File(path));
        return root.has("source") ? root.get("source").asInt() : 0;
    }
    public static void writeCSV(String path, List<String[]> rows) throws Exception {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Path.of(path)))){
            for (var r: rows){
                pw.println(Arrays.stream(r).map(s -> s.contains(",")?("\""+s.replace("\"","\"\"")+"\""):s)
                        .collect(Collectors.joining(",")));
            }
        }
    }
    public static void main(String[] args) throws Exception {
        String dataset = args.length>0? args[0] : "data/tasks.json";
        System.out.println("Dataset: "+dataset);
        Graph g = readJson(dataset);
        int source = readSource(dataset);

        SimpleMetrics M = new SimpleMetrics();
        Condensation.Result CR;
        try (Stopwatch sw = new Stopwatch("SCC + Condensation")){
            CR = Condensation.build(g, M);
        }
        var comps = new HashMap<Integer, List<Integer>>();
        for (int v=0; v<g.n(); v++){
            comps.computeIfAbsent(CR.compId[v], k-> new ArrayList<>()).add(v);
        }
        System.out.println("SCC count: "+CR.compCount);
        for (var e: comps.entrySet()){
            System.out.println("Component "+e.getKey()+" size="+e.getValue().size()+" nodes="+e.getValue());
        }

        List<Integer> topo;
        try (Stopwatch sw = new Stopwatch("Topological Sort (Kahn)")) {
            topo = TopologicalSort.kahn(CR.dag, M);
        }
        System.out.println("Topological order of components: "+topo);

        // Derived order of original tasks
        List<Integer> derived = new ArrayList<>();
        for (int c : topo) {
            var nodes = comps.getOrDefault(c, List.of());
            var sorted = new ArrayList<>(nodes);
            java.util.Collections.sort(sorted);
            derived.addAll(sorted);
        }
        System.out.println("Derived order of original tasks after SCC compression: " + derived);

        int srcComp = CR.compId[source];
        System.out.println("Source vertex = "+source+" -> component "+srcComp);

        DagShortestPaths sp;
        try (Stopwatch sw = new Stopwatch("DAG Shortest Paths")){
            sp = new DagShortestPaths(CR.dag, srcComp, M);
        }
        System.out.println("Shortest distances (by component): "+java.util.Arrays.toString(sp.dist()));
        for (int tComp=0; tComp<CR.dag.n(); tComp++){
            var path = sp.pathTo(tComp);
            if (!path.isEmpty()){
                System.out.println("Path to comp "+tComp+": "+path);
            }
        }

        DagLongestPath lp;
        try (Stopwatch sw = new Stopwatch("DAG Longest Path (critical)")){
            lp = new DagLongestPath(CR.dag, M);
        }
        int end = lp.argMax();
        var critical = lp.pathTo(end);
        System.out.println("Critical path (components): "+critical+" length="+lp.best()[end]);

        new File("out").mkdirs();
        writeCSV("out/metrics.csv", List.of(
                new String[]{"metric","value"},
                new String[]{"scc_dfs_calls", String.valueOf(M.get("scc_dfs_calls"))},
                new String[]{"scc_edges_seen", String.valueOf(M.get("scc_edges_seen"))},
                new String[]{"kahn_pushes", String.valueOf(M.get("kahn_pushes"))},
                new String[]{"kahn_pops", String.valueOf(M.get("kahn_pops"))},
                new String[]{"dag_relax", String.valueOf(M.get("dag_relax"))},
                new String[]{"dag_long_relax", String.valueOf(M.get("dag_long_relax"))}
        ));
    }
}
