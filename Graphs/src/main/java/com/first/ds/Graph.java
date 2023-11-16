package com.first.ds;

import com.first.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Class that represents a DiGraph
 * @param <T>
 */
public abstract class Graph<T> {
    final HashMap<T, List<Pair<T, Integer>>> node2AdjacencyList = new HashMap<>();
    final HashMap<T, Boolean> node2Visitation = new HashMap<>();
    final EdgeFactory<T> edgeFactory = new EdgeFactory<>();

    public static final boolean VISITED = true;
    public static final boolean UNVISITED = false;

    /**
     * Adds node to the adjacency map
     * @param nodeToAdd
     */
    public void add(T nodeToAdd) {
        node2AdjacencyList.putIfAbsent(nodeToAdd, new ArrayList<>());
        node2Visitation.putIfAbsent(nodeToAdd, UNVISITED);
    }

    /**
     * Utilizes DFS to get to the other node, nothing else
     * @param src
     * @param dest
     * @return List with the traversed nodes
     */
    public final List<T> getPath(T src, T dest) {
        if (!(node2AdjacencyList.containsKey(src) && node2AdjacencyList.containsKey(dest))) {
            throw new IllegalArgumentException("Both nodes must be on the graph! Try to use the add() method of the graph");
        }

        Stack<T> nodesToVisit = new Stack<>();
        List<T> path = new ArrayList<>();
        nodesToVisit.add(src);

        while (!nodesToVisit.empty()) {
            T topStackNode = nodesToVisit.pop();
            if (node2Visitation.get(topStackNode) == UNVISITED) {
                node2Visitation.put(topStackNode, VISITED);
                path.add(topStackNode);

                if (topStackNode.equals(dest)) {
                    return path;
                }

                for (Pair<T, Integer> pairWeightAdjacentNode : node2AdjacencyList.get(topStackNode)) {
                    T adjacentNode = pairWeightAdjacentNode.fst;
                    nodesToVisit.push(adjacentNode);
                }
            }
        }

        return null;
    }

}
