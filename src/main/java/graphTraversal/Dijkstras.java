package graphTraversal;

import utils.GraphNodeAL;

import java.util.ArrayList;
import java.util.List;

public class Dijkstras {
    public static <T> CostedPath findCheapestPathDijkstra(GraphNodeAL<?> startNode, T lookingFor) {
        CostedPath cp = new CostedPath();   // create result for the cheapest path
        List<GraphNodeAL<?>> encountered = new ArrayList<>(), unencountered = new ArrayList<>();    // to create encountered and unencountered lists
        startNode.nodeValue=0;  // set starting node value to zero
        unencountered.add(startNode);    // add the start node as the only vaule in the unencountered list to start
        GraphNodeAL<?> currentNode;

        do{ // loop until unencountered list is empty
            currentNode=unencountered.remove(0);     //get the first unencountered node (this is being got from a sorted list so it will have the lowest value)
            encountered.add(currentNode); // record current node in encountered list

            if (currentNode.data.equals(lookingFor)){   // found our goal - assemble list back to start and return it
                cp.pathList.add(currentNode);   // add the current goal node to the result list (only element
                cp.pathCost = currentNode.nodeValue;     //the total cheapest path cost is the node value of the current goal/node

                while(currentNode != startNode) {   // while we're not back to the start node
                    boolean foundPrevPathNode = false;  // use a flag to identify when the previous path node is identified


                }

            }
        }
    }
}
