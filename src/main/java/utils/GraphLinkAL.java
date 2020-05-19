package utils;

import java.io.Serializable;

public class GraphLinkAL implements Serializable {
    public GraphNodeAL<?> startNode, destNode;
    public int cost;
    public boolean easiest;
    public boolean historical;

    public <T> GraphLinkAL(GraphNodeAL<?> startNode, GraphNodeAL<?> destNode, int cost)  {
        this.startNode = startNode;
        this.destNode = destNode;
        this.cost = cost;
    }


    public <T> GraphLinkAL(GraphNodeAL<?> startNode, GraphNodeAL<?> destNode, int cost, boolean hist, boolean easy) {
        this.startNode = startNode;
        this.destNode = destNode;
        this.cost = cost;
        this.historical = hist;
        this.easiest = easy;
    }

//    public <T> GraphLinkAL(GraphNodeAL<?> destNode, int cost) {
//        this.startNode.links.add(this);
//        this.startNode.getAdjList().add(this.destNode);
//    }
}
