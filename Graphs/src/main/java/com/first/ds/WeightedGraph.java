package com.first.ds;

import com.first.utils.Pair;

import java.util.*;

public class WeightedGraph<T> extends Graph<T> {

    private static final int INF = -1;          // symbolizes infinity
    private static final int START_VALUE = 0;   // the first node must start with the lowest label
    private static final Object END = null;     // symbolizes the end of a list

    /**
     * This adds an edge no matter what, even though it's already on the graph.
     * TODO
     * @param src
     * @param dest
     * @param weight
     */
    public void addEdge(T src, T dest, int weight) {
        node2AdjacencyList.get(src).add(edgeFactory.getEdge(dest, weight));
        node2AdjacencyList.get(dest).add(edgeFactory.getEdge(src, weight));
    }


    /**
     * The returned map must be reversed iterated to get the shortest path from src to dest
     * CHECK getShortestPath
     * @param src
     * @return previousNodeMap, this map contains each shortest path
     * from the source node.
     */
    public HashMap<T, T> getPreviousNodesMap(T src) {
        if(!node2AdjacencyList.containsKey(src)){
            throw new IllegalArgumentException("Node must be on the graph, try using add()");
        }

        HashMap<T, T> previousNodesMap = new HashMap<>();
        HashMap<T, Integer> labels = new HashMap<>();
        initialize(src, labels);

        /**
         * Each node is labeled by the lightest incident edge, if a node is "unknown" yet
         * it is labeled by INF.
         * this implementation for getting the node with the lowest label has
         * an upperbound of n^2lgn (thinking about a k-connected-graph).
         */
        do{
            node2Visitation.put(src, VISITED);
            for (Pair<T, Integer> edge : node2AdjacencyList.get(src)) {
                if (node2Visitation.get(edge.fst) == UNVISITED
                        && (labels.get(edge.fst) == INF || edge.snd < labels.get(edge.fst))) {
                        labels.put(edge.fst, edge.snd);
                        previousNodesMap.put(edge.fst,src);
                }
            }
            src = getLowestLabelNode(labels);

        }while(src != END);

        return previousNodesMap;
    }

    /**
     * Initializes the labels map to use the dijkstra algorithm
     * @param startNode
     * @param labels
     */
    private void initialize(T startNode, HashMap<T, Integer> labels) {
        Set<T> nodes = node2AdjacencyList.keySet();
        for (T node : nodes) {
            labels.put(node, INF);
            if (node.equals(startNode)) {
                labels.put(node, START_VALUE);
            }
        }
    }

    /**
     * The priority queue has an insert time of O(lgn), each time we insert one
     * node less but stills linear, this can still be improved
     * @param labels
     * @return T, the node with the lowest label
     */
    private T getLowestLabelNode(HashMap<T, Integer> labels) {
        Set<T> nodes = labels.keySet();
        PriorityQueue<T> nodesPQ = new PriorityQueue<>((T node1, T node2) -> {
            if (labels.get(node1) == INF) {
                return 1;
            }
            if (labels.get(node2) == INF) {
                return -1;
            }
            return labels.get(node1) - labels.get(node2);
        });

        for (T node : nodes) {
            if (node2Visitation.get(node) == UNVISITED) {
                nodesPQ.add(node);
            }
        }
        // returns null if all nodes were visited
        return nodesPQ.poll();
    }

    /**
     *
     * @param src
     * @param dest
     * @return list, which is reversed, to get the shortest path from src to dest
     */
    public List<T> getShortestPath(T src, T dest){
        if (!(node2AdjacencyList.containsKey(src) && node2AdjacencyList.containsKey(dest))) {
            throw new IllegalArgumentException("Both nodes must be on the graph! Try to use the add() method of the graph");
        }

        List<T> list = new LinkedList<>();
        HashMap<T,T> previousNodesMap = getPreviousNodesMap(src);
        T previous = dest;

        while(previous != END){
            list.add(previous);
            previous = previousNodesMap.get(previous);
        }
        Collections.reverse(list);
        return list;
    }
}