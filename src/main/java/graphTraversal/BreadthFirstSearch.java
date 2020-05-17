package graphTraversal;

import utils.GraphLinkAL;
import utils.GraphNodeAL;
import utils.Landmark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BreadthFirstSearch {


    public static ArrayList<Integer> bfs(GraphNodeAL strtNode, GraphNodeAL dstNode, int width, int[] graphArray) {
        ArrayList<Integer> agenda = new ArrayList<>();
        ArrayList<Integer> newPath = new ArrayList<>();
        int destIndex = (int) (dstNode.x * width + dstNode.x);
        int startIndex = (int) (strtNode.y * width + strtNode.x);
        agenda.add(startIndex);
        graphArray[startIndex] = 1;
        int v, current;  // = graphArray[current];    // not sure if it goes here
        do {
            current = agenda.remove(0);
            if (current == destIndex) { //step 9
                int cn = destIndex;
                v = graphArray[destIndex];    // not sure if it goes here
                int totalDistance = v;
                System.out.println("Total Distance: " + totalDistance);
                newPath.add(0, cn);
                if (cn == startIndex)
                    return newPath;     //step 14
                else {
                    do {
                        if (graphArray[cn - 1] == v - 1) {
                            cn = cn - 1;
                            v= v -1;
                            newPath.add(cn);
                        } else if (graphArray[cn + 1] == v - 1) {
                            cn = cn + 1;
                            v= v -1;
                            newPath.add(cn);
                        } else if (graphArray[cn - width] == v - 1) {
                            cn = cn - width;
                            v= v -1;
                            newPath.add(cn);
                        } else {
                            cn = cn + width;
                            v= v -1;
                            newPath.add(cn);
                        }
                    } while (cn != startIndex);
                }
            } else {
                v = graphArray[current];    // not sure if it goes here
                if (graphArray[current + 1] == 0) {
                    graphArray[current + 1] = v + 1;
                    agenda.add(current + 1);
                }
                if (graphArray[current - 1] == 0) {
                    graphArray[current - 1] = v + 1;
                    agenda.add(current - 1);
                }
                if ((graphArray[current+ width] <= graphArray.length) && graphArray[current + width] == 0) {
                    graphArray[current + width] = v + 1;
                    agenda.add(current + width);
                }
                if ((graphArray[current - width] <= graphArray.length) && graphArray[current - width] == 0) {
                    graphArray[current - width] = v + 1;
                    agenda.add(current - width);
                }
            }
        }
        while (current != destIndex);
        return null;
    }

    public static <T> List<GraphNodeAL<?>> findPathBreadthFirst(GraphNodeAL<?> startNode, T lookingFor) {
        List<List<GraphNodeAL<?>>> agenda = new ArrayList<>(); // agenda comprised of a path lists here
        List<GraphNodeAL<?>> firstAgendaPath = new ArrayList<>(), resultPath;
        firstAgendaPath.add(startNode);
        agenda.add(firstAgendaPath);
        resultPath = findPathBreadthFirst(agenda, null, lookingFor);
        Collections.reverse(resultPath);
        return resultPath;
    }

    public static <T> List<GraphNodeAL<?>> findPathBreadthFirst(List<List<GraphNodeAL<?>>> agenda,
                                                                List<GraphNodeAL<?>> encountered, T lookingfor) {
        if (agenda.isEmpty()) return null; //Search failed
        List<GraphNodeAL<?>> nextPath = agenda.remove(0); //Get first item (next path to consider) off agenda
        GraphNodeAL<?> currentNode = nextPath.get(0); //The first item in the next path is the current node
        if (currentNode.data.equals(lookingfor))
            return nextPath; //If that's the goal, we've found our path (so return it)
        if (encountered == null)
            encountered = new ArrayList<>(); //First node considered in search so create new (empty)encountered list
        encountered.add(currentNode); //Record current node as encountered so it isn't revisited again
        for (GraphNodeAL<?> adjNode : currentNode.adjNodeList) //For each adjacent node
            if (!encountered.contains(adjNode)) { //If it hasn't already been encountered
                List<GraphNodeAL<?>> newPath = new ArrayList<>(nextPath); //Create a new path list as a copy of
//the current/next path
                newPath.add(0, adjNode); //And add the adjacent node to the front of the new copy
                agenda.add(newPath); //Add the new path to the end of agenda (end->BFS!)
            }
        return findPathBreadthFirst(agenda, encountered, lookingfor); //Tail call
    }


}

// clean from notes
//    public static <T> List<GraphNodeAL<?>> findPathBreadthFirst(List<List<GraphNodeAL<?>>> agenda,
//                                                                List<GraphNodeAL<?>> encountered, T lookingfor){
//        if(agenda.isEmpty()) return null; //Search failed
//        List<GraphNodeAL<?>> nextPath=agenda.remove(0); //Get first item (next path to consider) off agenda
//        GraphNodeAL<?> currentNode=nextPath.get(0); //The first item in the next path is the current node
//        if(currentNode.data.equals(lookingfor)) return nextPath; //If that's the goal, we've found our path (so return it)
//        if(encountered==null) encountered=new ArrayList<>(); //First node considered in search so create new (empty)encountered list
//        encountered.add(currentNode); //Record current node as encountered so it isn't revisited again
//        for(GraphNodeAL<?> adjNode : currentNode.adjNodeList) //For each adjacent node
//            if(!encountered.contains(adjNode)) { //If it hasn't already been encountered
//                List<GraphNodeAL<?>> newPath=new ArrayList<>(nextPath); //Create a new path list as a copy of
////the current/next path
//                newPath.add(0,adjNode); //And add the adjacent node to the front of the new copy
//                agenda.add(newPath); //Add the new path to the end of agenda (end->BFS!)
//            }
//        return findPathBreadthFirst(agenda,encountered,lookingfor); //Tail call
//    }


//    public static <T> List<GraphNodeAL<?>> findPathBreadthFirst(GraphNodeAL<?> startNode, T lookingFor) {
//        List<List<GraphNodeAL<?>>> agenda = new ArrayList<>(); // agenda comprised of a path lists here
//        List<GraphNodeAL<?>> firstAgendaPath = new ArrayList<>(), resultPath;
//        firstAgendaPath.add(startNode);
//        agenda.add(firstAgendaPath);
//        resultPath = findPathBreadthFirst(agenda, null, lookingFor);
//        Collections.reverse(resultPath);
//        return resultPath;
//    }
//
//    public static <T> List<GraphNodeAL<?>> findPathBreadthFirst(List<List<GraphNodeAL<?>>> agenda, List<GraphNodeAL<?>> encountered, T lookingFor) {
//        if (agenda.isEmpty()) return null;   //search fails
//        List<GraphNodeAL<?>> nextPath = agenda.remove(0); // to get first item(next path to consider) off of agenda
//        GraphNodeAL<?> currentNode = nextPath.get(0);   //the first item in the next path is the current node
//        if (currentNode.data.equals(lookingFor)) return nextPath; //return path as we've found our goal
//        if (encountered == null)
//            encountered = new ArrayList<>();// first node considered in search so create a new encountered arraylist
//        encountered.add(currentNode);   // record current node as encountered so it isn't revisited again
//
//        // issue with this is this is code from before it was changed for edge search #i think it should be changed from nodes to edges
//        for (GraphLinkAL adjLink : currentNode.adjList)  //for each adjacent node
//            if (!encountered.contains(adjLink)) {    //if it hasn't already been encountered
//                List<GraphNodeAL> newPath = new ArrayList<GraphNodeAL>(nextPath);   //creates a new arraylist as a copy of the current/next path
//
//                newPath.add(0, adjLink); //and add the adjacent node to the front of the new copy
////                newPath.add(0, adjLink); //and add the adjacent node to the front of the new copy
//                agenda.add(nextPath);    //  add the new path to the end of the agenda (end -> BFS)
//            }
//        return findPathBreadthFirst(agenda, encountered, lookingFor); //tail call
//
//    }
//}
