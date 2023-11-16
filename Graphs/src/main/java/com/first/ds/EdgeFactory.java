package com.first.ds;

import com.first.utils.Pair;

public class EdgeFactory<T> {

    public static final int UNWEIGHTED = 0;

    public Pair<T, Integer> getEdge(T dest) {
        return new Pair<>(dest,UNWEIGHTED);
    }

    public Pair<T, Integer> getEdge(T dest, int weight) {
        return new Pair<>(dest, weight);
    }
}
