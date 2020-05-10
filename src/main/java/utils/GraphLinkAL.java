package utils;

public class GraphLinkAL {
    public GraphNodeAL<?> startNode, destNode;
    public int cost;

    public <T> GraphLinkAL(GraphNodeAL<?> startNode, GraphNodeAL<?> destNode, int cost) {
        this.startNode = startNode;
        this.destNode = destNode;
        this.cost = cost;
//        this.startNode.adjList.add(this);
//        this.startNode.getAdjList().add(this.destNode);
    }

//    public <T> GraphLinkAL(GraphNodeAL<?> destNode, int cost) {
//        this.startNode.links.add(this);
//        this.startNode.getAdjList().add(this.destNode);
//    }
}
