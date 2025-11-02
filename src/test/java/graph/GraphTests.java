package graph;

import graph.common.Graph;
import graph.common.SimpleMetrics;
import graph.common.Condensation;
import graph.scc.TarjanSCC;
import graph.topo.TopologicalSort;
import graph.dagsp.DagShortestPaths;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class GraphTests {
    @Test
    public void testSCCSimpleCycle(){
        Graph g = new Graph(3,true);
        g.addEdge(0,1,1); g.addEdge(1,2,1); g.addEdge(2,0,1);
        var scc = new TarjanSCC(g, new SimpleMetrics());
        assertEquals(1, scc.compCount());
    }

    @Test
    public void testTopoOrder(){
        Graph g = new Graph(4,true);
        g.addEdge(0,1,1); g.addEdge(1,2,1); g.addEdge(0,3,1);
        var order = TopologicalSort.kahn(g, new SimpleMetrics());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(0) < order.indexOf(3));
        assertTrue(order.indexOf(1) < order.indexOf(2));
    }

    @Test
    public void testDagShortest(){
        Graph g = new Graph(4,true);
        g.addEdge(0,1,1); g.addEdge(1,2,2); g.addEdge(0,2,5); g.addEdge(2,3,1);
        var sp = new DagShortestPaths(g, 0, new SimpleMetrics());
        assertEquals(0.0, sp.dist()[0], 1e-9);
        assertEquals(1.0, sp.dist()[1], 1e-9);
        assertEquals(3.0, sp.dist()[2], 1e-9);
        assertEquals(4.0, sp.dist()[3], 1e-9);
        var path = sp.pathTo(3);
        assertEquals(List.of(0,1,2,3), path);
    }
}
