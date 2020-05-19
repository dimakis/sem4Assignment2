package graphTraversal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.GraphNodeAL;
import utils.Landmark;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class DijkstrasTest {

    Dijkstras dijkstras = new Dijkstras();
    GraphNodeAL stNode, midNode, edNode;
    ArrayList<GraphNodeAL> avoid;

    @Before
    public void setUp() throws Exception {
        stNode = new GraphNodeAL(new Landmark(796, 409, "Point_1", false));
        midNode = new GraphNodeAL(new Landmark(761, 217, "Point_3", false));
        edNode = new GraphNodeAL(new Landmark(400, 400, "Point_2", false));
        stNode.connectToNodeUndirected(stNode, midNode, 10);
        midNode.connectToNodeUndirected(midNode, edNode, 10);
        stNode.connectToNodeUndirected(stNode, edNode, 100);
        avoid = new ArrayList<>();
        avoid.add(midNode);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findCheapestPathDijkstra() {
        assertEquals((dijkstras.findCheapestPathDijkstra(stNode, edNode.data)).pathCost,20);
    }

    @Test
    public void findCheapestPathDijkstraWithAvoid() {
        assertEquals((dijkstras.findCheapestPathDijkstra(stNode, edNode.data,avoid)).pathCost,100);
    }
}
