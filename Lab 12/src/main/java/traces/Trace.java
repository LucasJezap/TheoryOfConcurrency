package traces;

import java.util.*;

class Trace {
    final HashSet<String> alphabet;
    final LinkedHashMap<String, HashSet<String>> I;
    LinkedHashMap<String, HashSet<String>> D;
    final String word;
    LinkedHashSet<String> traces;
    StringBuilder foataForm;
    DependenceGraph dgraph;
    StringBuilder foataFromGraphForm;

    Trace(final HashSet<String> alphabet, final LinkedHashMap<String, HashSet<String>> I, final String word) {
        this.alphabet = alphabet;
        this.I = I;
        this.D = new LinkedHashMap<>();
        this.word = word;
        this.traces = new LinkedHashSet<>();
        this.foataForm = new StringBuilder();
        this.dgraph = new DependenceGraph();
        this.foataFromGraphForm = new StringBuilder();
    }

    void getDependenceRelation() {
        for (String a : alphabet) {
            for (String b : alphabet) {
                if (!I.containsKey(a) || !I.get(a).contains(b)) {
                    if (!D.containsKey(a)) {
                        HashSet<String> hs = new HashSet<>();
                        hs.add(b);
                        D.put(a, hs);
                    } else {
                        D.get(a).add(b);
                    }
                }
            }
        }
    }

    void getTraces(String word) {
        traces.add(word);

        for (int i = 0; i < word.length() - 1; i++) {
            String key = Character.toString(word.charAt(i));
            String swapped = word.substring(0, i) + word.charAt(i + 1) + word.charAt(i);
            if (i + 2 < word.length())
                swapped += word.substring(i + 2);
            if (I.containsKey(key) && I.get(key).contains(Character.toString(word.charAt(i + 1)))
                    && !traces.contains(swapped)) {
                getTraces(swapped);
                i++;
            }
        }
    }

    void getFoataForm() {
        LinkedHashMap<String, Stack<String>> stacks = new LinkedHashMap<>();
        for (String c : alphabet)
            stacks.put(c, new Stack<>());

        for (int i = word.length() - 1; i >= 0; i--) {
            String c = Character.toString(word.charAt(i));
            stacks.get(c).push(c);
            for (String ch : alphabet) {
                if (!c.equals(ch) && (!I.containsKey(c) || !I.get(c).contains(ch))) {
                    stacks.get(ch).push("*");
                }
            }
        }

        while (true) {
            StringBuilder new_foata_block = new StringBuilder("(");
            boolean anyStackNotEmpty = false;
            HashMap<String, Integer> poppedMarkers = new HashMap<>();
            for (String c : alphabet)
                poppedMarkers.put(c, 0);

            for (String key : stacks.keySet()) {
                if (stacks.get(key).empty())
                    continue;
                anyStackNotEmpty = true;
                if (stacks.get(key).peek().equals("*") || poppedMarkers.get(key) != 0)
                    continue;

                String top = stacks.get(key).pop();
                new_foata_block.append(top);

                for (String c : D.get(top)) {
                    if (!c.equals(top) && !stacks.get(c).empty()) {
                        stacks.get(c).pop();
                        poppedMarkers.put(c, poppedMarkers.get(c) + 1);
                    }
                }
            }

            new_foata_block.append(")");
            if (!anyStackNotEmpty) break;
            foataForm.append(new_foata_block);
        }
    }

    void getDependenceGraph() {
        dgraph.createDependenceGraph(word, D);
    }

    void getFoataFromGraph() {
        dgraph.createTopologicalSort();
        ArrayList<String> topological = dgraph.topologicalSort;
        int index = 0;
        while (index < word.length()) {
            StringBuilder new_foata_chunk = new StringBuilder("(");
            new_foata_chunk.append(topological.get(index));

            int start_index = index++;
            while (index < word.length()) {
                boolean isIndependent = true;
                for (int i = start_index; i < index; i++) {
                    if (!I.containsKey(topological.get(index))
                            || !I.get(topological.get(index)).contains(topological.get(i))) {
                        isIndependent = false;
                        break;
                    }
                }
                if (isIndependent)
                    new_foata_chunk.append(topological.get(index++));
                else
                    break;
            }
            new_foata_chunk.append(")");
            foataFromGraphForm.append(new_foata_chunk);
        }
    }

    void printTraceInfo() {
        System.out.println("Alphabet: {" + String.join(", ", alphabet) + "}");

        ArrayList<String> tmp = new ArrayList<>();
        for (String key : I.keySet())
            for (String value : I.get(key))
                tmp.add("(" + key + "," + value + ")");
        System.out.println("I: {" + String.join(", ", tmp) + "}");

        System.out.println("Input word: " + word);

        ArrayList<String> tmp2 = new ArrayList<>();
        for (String key : D.keySet())
            for (String value : D.get(key))
                tmp2.add("(" + key + "," + value + ")");
        System.out.println("D: {" + String.join(", ", tmp2) + "}");
        System.out.println("Traces: {" + String.join(", ", traces) + "}");
        System.out.println("Foata form: " + foataForm);
        System.out.println("Graph:\n" + dgraph.getGraphInfo());
        System.out.println("Foata (from Graph) form: " + foataFromGraphForm);
    }
}
