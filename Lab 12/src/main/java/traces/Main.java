package traces;

import java.util.HashSet;
import java.util.LinkedHashMap;

class Main {

    public static void main(String[] args) {
        HashSet<String> alphabet = Input.getAlphabetFromInput();
        LinkedHashMap<String, HashSet<String>> I = Input.getIndependenceRelationFromInput();
        String word = Input.getInputWord();
        Input.checkInputCorrectness(alphabet, I, word);

        Trace trace = new Trace(alphabet, I, word);
        trace.getDependenceRelation(); // task 1
        trace.getTraces(word); // task 2
        trace.getFoataForm(); // task 3
        trace.getDependenceGraph(); // task 4
        trace.getFoataFromGraph(); // task 5

        System.out.println("\n\nSUMMARY:");
        trace.printTraceInfo();
    }
}
