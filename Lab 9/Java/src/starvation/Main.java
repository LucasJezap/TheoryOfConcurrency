package starvation;
// ŁUKASZ JEZAPKOWICZ LAB 8

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int numberOfPhilosophers = 5;
    private static final int numberOfSeconds = 5;
    static ArrayList<Integer> accesses = new ArrayList<>(Collections.nCopies(numberOfPhilosophers, 0));

    public static void main(String[] args) throws InterruptedException {
        Table table = new Table(numberOfPhilosophers);
        ExecutorService philosophers = Executors.newFixedThreadPool(numberOfPhilosophers);
        for (int i = 0; i < numberOfPhilosophers; i++) {
            philosophers.submit(new Philosopher(table, i));
        }

        System.out.println("The program will work for " + numberOfSeconds + " seconds.");
        for (int i = 0; i < numberOfSeconds; i++) {
            Thread.sleep(1000);
            System.out.println("Remaining time: " + (numberOfSeconds - i));
        }

        philosophers.shutdownNow();
        philosophers.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        for (int i=0; i < numberOfPhilosophers; i++) {
            System.out.println("Philosopher " + i + " has eaten " + accesses.get(i) + " times.");
        }

    }


}
