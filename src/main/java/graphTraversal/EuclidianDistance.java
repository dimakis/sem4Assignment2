package graphTraversal;

import utils.GraphNodeAL;

import java.util.ArrayList;
import java.util.Collections;

public class EuclidianDistance {
    public static ArrayList<Integer> euclideanSearch(GraphNodeAL strtNode, GraphNodeAL dstNode, int width, int[] graphArray, String mode) {
        ArrayList<Integer> agenda = new ArrayList<>();
        String euc = "Euclidean";
        String feelin = "Feeling Lucky";
        String taxi = "Taxi";
        String choice = "";
        int destIndex = (int) (dstNode.y * width + dstNode.x);
        int startIndex = (int) (strtNode.y * width + strtNode.x);
        agenda.add(startIndex);
        graphArray[startIndex] = 1;
        int v, current;
        do {
            if (mode.equals("Euclidean")) {
                Collections.sort(agenda);
                choice = euc;
            }
            else if (mode.equals("FeelingLucky")) {
                Collections.shuffle(agenda);
                choice = feelin;
            }
            else {
                Collections.reverse(agenda);
                choice = taxi;
            }
            current = agenda.remove(0);
            if (current == destIndex) { //step 9
                int totalDistance = graphArray[current] - 1;
                ArrayList<Integer> newPath = new ArrayList<>();
                int cn = destIndex;
                v = graphArray[destIndex];
                System.out.println("Total Distance using "+ choice + " Search: " + totalDistance);

                newPath.add(0, cn);
                if (cn == startIndex)
                    return newPath;     //step 14
                else {
                    // builds the list from destination node to start node
                    do {
                        if (cn - 1 >= 0 && cn - 1 % width != 0 && graphArray[cn - 1] == v - 1) {
                            cn = cn - 1;
                            v = v - 1;
                            newPath.add(cn);
                        } else if (cn + 1 < graphArray.length && cn % width >= 0 && graphArray[cn + 1] == v - 1) {
                            cn = cn + 1;
                            v = v - 1;
                            newPath.add(cn);
                        } else if (cn - width >= 0 && graphArray[cn - width] == v - 1) {
                            cn = cn - width;
                            v = v - 1;
                            newPath.add(cn);
                            Collections.shuffle(newPath);
                        } else if (cn + width < graphArray.length && graphArray[cn + width] == v - 1) {
                            cn = cn + width;
                            v = v - 1;
                            newPath.add(cn);
                        }
                    } while (cn != startIndex);
                    return newPath;
                }
            } else {
                // builds paths from destination node
                v = graphArray[current];    // not sure if it goes here
                if (current + 1 < graphArray.length && current % width != 0 && graphArray[current + 1] == 0) {
                    graphArray[current + 1] = v + 1;
                    agenda.add(current + 1);
                }
                if (current - 1 >= 0 && current - 1 % width != 0 && graphArray[current - 1] == 0) {
                    graphArray[current - 1] = v + 1;
                    agenda.add(current - 1);
                }
                if (current + width < graphArray.length && graphArray[current + width] == 0) {
                    graphArray[current + width] = v + 1;
                    agenda.add(current + width);
                }
                if (current - width >= 0 && graphArray[current - width] == 0) {
                    graphArray[current - width] = v + 1;
                    agenda.add(current - width);
                }
            }
        }
        while (!agenda.isEmpty());
        return null;
    }
}
