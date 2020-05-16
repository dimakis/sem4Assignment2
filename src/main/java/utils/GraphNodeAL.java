package utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GraphNodeAL<T> implements Serializable {
    public T data;
    public int nodeValue = Integer.MAX_VALUE;
    //    public ArrayList<GraphLinkAL> links;
    public ArrayList<GraphLinkAL> adjList;


    public GraphNodeAL(T data) {
        this.data = data;
//        this.links = new ArrayList<>();
        this.adjList = new ArrayList<>();
    }

    public GraphNodeAL(Landmark landmark) {
        this.data = (T) landmark;
//        this.data = this.getData();
//        this.links = new ArrayList<>();
        this.adjList = new ArrayList<>();
        this.setNodeValue(this.getNodeValue());
    }

//    public void readObject(ObjectInputStream aInputStream) throws IOException, ClassNotFoundException {
//        data = (T) aInputStream.readObject();
//        adjList = (ArrayList<GraphLinkAL>) aInputStream.readObject();
//
//    }


//    public void connectToNodeDirected(GraphNodeAL<T> destNode, int cost) {
//        adjList.add(new GraphLinkAL(destNode, cost));
//    }
//
//    public void connectToNodeUndirected(GraphNodeAL<T> destNode, int cost) {
//        adjList.add(new GraphLinkAL(destNode, cost));
//        destNode.adjList.add(new GraphLinkAL(this, cost));
//    }

    public void connectToNodeDirected(GraphNodeAL<T> startNode, GraphNodeAL<T> destNode, int cost) {
        adjList.add(new GraphLinkAL(startNode, destNode, cost));
    }

    public void connectToNodeUndirected(GraphNodeAL<T> startNode, GraphNodeAL<T> destNode, int cost) {
        adjList.add(new GraphLinkAL(startNode, destNode, cost));
        destNode.adjList.add(new GraphLinkAL(startNode, destNode, cost));
    }

    public void setData(T data) {
        this.data = data;
    }

//    public double getX() {
//        return x;
//    }
//
//    public void setX(double x) {
//        this.x = x;
//    }
//
//    public double getY() {
//        return y;
//    }
//
//    public void setY(double y) {
//        this.y = y;
//    }
//


    public int getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(int nodeValue) {
        this.nodeValue = nodeValue;
    }

    public List<GraphLinkAL> getAdjList() {
        return adjList;
    }

    public void setAdjList(ArrayList<GraphLinkAL> adjList) {
        this.adjList = adjList;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
