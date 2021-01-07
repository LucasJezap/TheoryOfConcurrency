package traces;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Scanner;

class Input {

    static HashSet<String> getAlphabetFromInput() {
        HashSet<String> alphabet = new HashSet<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("How big is the alphabet?");
        int tmp = scanner.nextInt();
        System.out.println("Please enter the alphabet (with spaces):");
        for (int i = 0; i < tmp; i++) {
            alphabet.add(scanner.next());
        }
        return alphabet;
    }

    static LinkedHashMap<String, HashSet<String>> getIndependenceRelationFromInput() {
        LinkedHashMap<String, HashSet<String>> independent = new LinkedHashMap<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("How big is the independence relation?");
        int tmp = scanner.nextInt();
        System.out.println("Please enter the pairs of characters (with spaces):");
        for (int i = 0; i < tmp; i++) {
            String key = scanner.next();
            String value = scanner.next();
            if (independent.containsKey(key)) {
                independent.get(key).add(value);
            } else {
                HashSet<String> hs = new HashSet<>();
                hs.add(value);
                independent.put(key, hs);
            }
        }
        return independent;
    }

    static String getInputWord() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the input word:");
        return scanner.next();
    }

    static void checkInputCorrectness(final HashSet<String> alphabet, final LinkedHashMap<String, HashSet<String>> I, final String word) {
        boolean viable = true;
        for (String key : I.keySet()) {
            if (!alphabet.contains(key)) viable = false;
            for (String value : I.get(key))
                if (!alphabet.contains(value)) {
                    viable = false;
                    break;
                }
            if (!viable) break;
        }

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!alphabet.contains(Character.toString(c))) {
                viable = false;
                break;
            }
        }

        if (!viable) {
            System.out.println("Given input is not correct!");
            System.exit(-1);
        }
    }
}
