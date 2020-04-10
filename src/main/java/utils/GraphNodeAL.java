package utils;

import java.util.ArrayList;
import java.util.List;

public class GraphNodeAL<T> {
    public T data;
    public double x, y;
    public int nodeValue = Integer.MAX_VALUE;

    public List<GraphLinkAL> adjList = new ArrayList<>();

    public GraphNodeAL(T data, double xCoord, double yCoord) {
        this.data = data;
        this.y = yCoord;
        this.x = xCoord;
    }

    public GraphNodeAL() {

    }

    public void connectToNodeDirected(GraphNodeAL<T> destNode, int cost) {
        adjList.add(new GraphLinkAL(destNode, cost));
    }

    public void connectToNodeUndirected(GraphNodeAL<T> destNode, int cost) {
        adjList.add(new GraphLinkAL(destNode, cost));
        destNode.adjList.add(new GraphLinkAL(this, cost));
    }
}
