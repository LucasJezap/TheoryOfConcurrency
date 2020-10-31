
// ŁUKASZ JEZAPKOWICZ LAB 4

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    //public static final int M = 10000;
    public static final int M = 100000;

//    public static final int producersNumber = 100;
//    public static final int consumersNumber = 100;
    public static final int producersNumber = 1000;
    public static final int consumersNumber = 1000;

    private static final int bufferStartValue = 0;
    private static final int numberOfSeconds = 30;

    public static final boolean isFair = true;
    public static final boolean sameProb = true;
    public static final ArrayList<Integer> producersAccesses = new ArrayList<>(Collections.nCopies(M / 2 + 1, 0));
    public static final ArrayList<Integer> consumersAccesses = new ArrayList<>(Collections.nCopies(M / 2 + 1, 0));

    public static int generateNumber() {
        if (sameProb) {
            return new Random().nextInt((int) Math.floor(M / 2.0)) + 1;
        } else {
            return (int) Math.floor((M / 2.0 + 1) * (Math.pow(new Random().nextDouble(), 1.5)));
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Buffer buffer = new Buffer(bufferStartValue, M);
        Histogram histogram = new Histogram(M);
        CsvHandler csv = new CsvHandler();
        DataGenerator dg = new DataGenerator();
        ExecutorService producersAndConsumers = Executors.newFixedThreadPool(producersNumber + consumersNumber);
        for (int i = 0; i < producersNumber; i++) {
            producersAndConsumers.submit(new Producer(buffer));
        }
        for (int i = 0; i < consumersNumber; i++) {
            producersAndConsumers.submit(new Consumer(buffer));
        }

        System.out.println("The program will work for " + numberOfSeconds + " seconds.");
        for (int i = 0; i < numberOfSeconds; i++) {
            Thread.sleep(1000);
            System.out.println("Remaining time: " + (numberOfSeconds - i));
        }

        producersAndConsumers.shutdownNow();
        producersAndConsumers.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        System.out.println("Current Configuration:");
        System.out.println("Buffer size: " + M);
        if (producersNumber == 100) {
            System.out.println("PC Config: 100P + 100C");
        } else {
            System.out.println("PC Config: 1000P + 1000C");
        }
        if (isFair) {
            System.out.println("Is buffer fair: yes");
        } else {
            System.out.println("Is buffer fair: no");
        }
        if (sameProb) {
            System.out.println("Same portion probability: yes");
        } else {
            System.out.println("Same portion probability: no");
        }
        histogram.printValues();

        List<String[]> dataLines = dg.generateDataLines();

        csv.saveToCsvFile(dataLines);
    }


}
