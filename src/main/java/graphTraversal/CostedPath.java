package graphTraversal;

import utils.GraphNodeAL;

import java.util.ArrayList;
import java.util.List;

// class to hold a costedPath which is a list of GraphNodeAL objects and a total cost attribute
public class CostedPath {
    public int pathCost=0;
    public List<GraphNodeAL<?>> pathList = new ArrayList<>();

}
