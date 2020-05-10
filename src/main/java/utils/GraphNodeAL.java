package utils;

import java.util.ArrayList;
import java.util.List;

public class GraphNodeAL<T> {
    public T data;
    public double x, y;
    public String landmarkName;
    public int nodeValue = Integer.MAX_VALUE;
    public ArrayList<GraphLinkAL> links;
    public List<GraphLinkAL> adjList;


    public GraphNodeAL(T data, double xCoord, double yCoord) {
        this.data = data;
        this.y = yCoord;
        this.x = xCoord;
        this.links = new ArrayList<>();
        this.adjList = new ArrayList<>();
    }

    public GraphNodeAL(Landmark trevi) {

    }

    public void connectToNodeDirected(GraphNodeAL<T> destNode, int cost) {
        adjList.add(new GraphLinkAL(destNode, cost));
    }

    public void connectToNodeUndirected(GraphNodeAL<T> destNode, int cost) {
        adjList.add(new GraphLinkAL(destNode, cost));
        destNode.adjList.add(new GraphLinkAL(this, cost));
    }

    public void setData(T data) {
        this.data = data;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getLandmarkName() {
        return landmarkName;
    }

    public void setLandmarkName(String landmarkName) {
        this.landmarkName = landmarkName;
    }

    public int getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(int nodeValue) {
        this.nodeValue = nodeValue;
    }

    public List<GraphLinkAL> getAdjList() {
        return adjList;
    }

    public void setAdjList(List<GraphLinkAL> adjList) {
        this.adjList = adjList;
    }

    public T getData() {
        return null;
    }
}
