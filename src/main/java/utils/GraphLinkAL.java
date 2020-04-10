package utils;

public class GraphLinkAL extends GraphNodeAL {
    public GraphNodeAL<?> destNode;
    public int cost;

    public GraphLinkAL(GraphNodeAL<?> destNode,int cost)    {
        super();
        this.destNode = destNode;
        this.cost = cost;
    }
}
