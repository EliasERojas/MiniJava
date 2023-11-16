package com.first;

import com.first.ds.WeightedGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Person elias = new Person("elias", 22);
        Person santi = new Person("santi", 26);
        Person dani = new Person("dani", 38);
        Person andres = new Person("andres", 20);
        Person teo = new Person("teo", 20);
        WeightedGraph<Person> network = new WeightedGraph<>();


        network.add(elias);
        network.add(santi);
        network.add(dani);
        network.add(teo);
        network.add(andres);

        network.addEdge(elias,santi,3);
        network.addEdge(santi,dani,2);
        network.addEdge(santi,teo,13);
        network.addEdge(elias,andres,15);
        network.addEdge(andres,teo,1);
        network.addEdge(andres,santi,4);

        List<Person> shortestPath = network.getShortestPath(elias,teo);

    }
}