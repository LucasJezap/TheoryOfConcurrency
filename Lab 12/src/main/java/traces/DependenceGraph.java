package traces;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Stack;

class DependenceGraph {
    final ArrayList<Integer> vertices;
    final LinkedHashMap<Integer, String> mapping;
    final LinkedHashMap<Integer, HashSet<Integer>> edges;
    ArrayList<String> topologicalSort;

    DependenceGraph() {
        this.vertices = new ArrayList<>();
        this.mapping = new LinkedHashMap<>();
        this.edges = new LinkedHashMap<>();
        this.topologicalSort = new ArrayList<>();
    }

    private void DFS(HashSet<Integer> visited, int vertex) {
        visited.add(vertex);
        if (!edges.containsKey(vertex))
            return;
        for (Integer neighbour : edges.get(vertex)) {
            if (!visited.contains(neighbour))
                this.DFS(visited, neighbour);
        }
    }

    private void minimizeDependenceGraph() {
        for (Integer vertex : vertices) {  // for every vertex we do DFS of all his neighbours
            if (!edges.containsKey(vertex))
                continue;
            HashSet<Integer> toDelete = new HashSet<>();
            for (Integer neighbour : edges.get(vertex)) {
                if (toDelete.contains(neighbour))
                    continue;
                HashSet<Integer> visited = new HashSet<>();
                this.DFS(visited, neighbour);
                for (Integer vertexVisited : visited) {
                    if (!vertexVisited.equals(neighbour)) {
                        toDelete.add(vertexVisited);
                    }
                }
            }

            for (Integer neighbour : toDelete) {
                edges.get(vertex).remove(neighbour);
            }
        }
    }

    void createDependenceGraph(final String word, final LinkedHashMap<String, HashSet<String>> D) {
        for (int i = 0; i < word.length(); i++) {
            vertices.add(i + 1);
            mapping.put(i + 1, Character.toString(word.charAt(i)));
            // now adding edges
            for (int j = 0; j < i; j++) {
                String tau1 = mapping.get(j + 1);
                String tau2 = mapping.get(i + 1);
                if (D.containsKey(tau1) && D.get(tau1).contains(tau2)) {
                    if (!edges.containsKey(j + 1)) {
                        HashSet<Integer> hs = new HashSet<>();
                        hs.add(i + 1);
                        edges.put(j + 1, hs);
                    } else {
                        edges.get(j + 1).add(i + 1);
                    }
                }
            }
        }
        this.minimizeDependenceGraph();
    }

    StringBuilder getGraphInfo() {
        StringBuilder sb = new StringBuilder("digraph g{\n");
        for (Integer key : edges.keySet()) {
            for (Integer value : edges.get(key)) {
                sb.append(key).append(" -> ").append(value).append("\n");
            }
        }
        for (Integer key : mapping.keySet()) {
            sb.append(key).append("[label=").append(mapping.get(key)).append("]\n");
        }
        sb.append("}");

        return sb;
    }

    private void topologicalDFS(HashSet<Integer> visited, Stack<Integer> tsort, int vertex) {
        visited.add(vertex);
        if (edges.containsKey(vertex)) {
            for (Integer neighbour : edges.get(vertex)) {
                if (!visited.contains(neighbour))
                    this.topologicalDFS(visited, tsort, neighbour);
            }
        }
        tsort.push(vertex);
    }

    void createTopologicalSort() {
        HashSet<Integer> visited = new HashSet<>();
        Stack<Integer> tsort = new Stack<>();
        for (Integer vertex : vertices) {  // for every vertex we do DFS of all his neighbours
            if (!visited.contains(vertex))
                this.topologicalDFS(visited, tsort, vertex);
        }
        while (!tsort.empty())
            topologicalSort.add(mapping.get(tsort.pop()));
    }
}
