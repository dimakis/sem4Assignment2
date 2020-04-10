package graphTraversal;

import utils.GraphNodeAL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BreadthFirstSearch {
    public static <T> List<GraphNodeAL<?>> findPathBreadthFirst(GraphNodeAL<?> startNode, T lookingFor) {
        List<List<GraphNodeAL<?>>> agenda = new ArrayList<>(); // agenda comprised of a path lists here
        List<GraphNodeAL<?>> firstAgendaPath = new ArrayList<>(), resultPath;
        firstAgendaPath.add(startNode);
        agenda.add(firstAgendaPath);
        resultPath = findPathBreadthFirst(agenda, null, lookingFor);
        Collections.reverse(resultPath);
        return resultPath;
    }

    public static <T> List<GraphNodeAL<?>> findPathBreadthFirst(List<List<GraphNodeAL<?>>> agenda, List<GraphNodeAL<?>> encountered, T lookingFor) {
        if (agenda.isEmpty()) return null;   //search fails
        List<GraphNodeAL<?>> nextPath = agenda.remove(0); // to get first item(next path to consider) off of agenda
        GraphNodeAL<?> currentNode = nextPath.get(0);   //the first item in the next path is the current node
        if (currentNode.data.equals(lookingFor)) return nextPath; //return path as we've found our goal
        if (encountered == null)
            encountered = new ArrayList<>();// first node considered in search so create a new encountered arraylist
        encountered.add(currentNode);   // record current node as encountered so it isn't revisited again

        // issue with this is this is code from before it was changed for edge search #i think it should be changed from nodes to edges
        for (GraphNodeAL<?> adjNode : currentNode.adjList)  //for each adjacent node
            if (!encountered.contains(adjNode)) {    //if it hasn't already been encountered
                List<GraphNodeAL<?>> newPath = new ArrayList<>(nextPath);   //creates a new arraylist as a copy of the current/next path
                newPath.add(0, adjNode); //and add the adjacent node to the front of the new copy
                agenda.add(newPath);    //  add the new path to the end of the agenda (end -> BFS)
            }
        return findPathBreadthFirst(agenda, encountered, lookingFor); //tail call

    }
}
