package utils;

public class GraphLinkAL {
    public GraphNodeAL<?> startNode, destNode;
    public int cost;

    public GraphLinkAL(GraphNodeAL startNode,GraphNodeAL<?> destNode,int cost)    {
        this.startNode = startNode;
        this.destNode = destNode;
        this.cost = cost;
    }

    public <T> GraphLinkAL(GraphNodeAL destNode, int cost) {

    }
}
