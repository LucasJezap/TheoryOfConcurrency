
// ŁUKASZ JEZAPKOWICZ LAB 3

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int M = 16384;
    private static final int producersNumber = 5;
    private static final int consumersNumber = 5;
    private static final int bufferStartValue = 0;
    private static final int numberOfSeconds = 10;

    public static void main(String[] args) throws InterruptedException, IOException {
        Buffer buffer = new Buffer(bufferStartValue, M);
        Histogram histogram = new Histogram(producersNumber, consumersNumber);
        ExecutorService producersAndConsumers = Executors.newFixedThreadPool(producersNumber + consumersNumber);
        for (int i = 0; i < producersNumber; i++) {
            producersAndConsumers.submit(new Producer(buffer, histogram, i));
        }
        for (int i = 0; i < consumersNumber; i++) {
            producersAndConsumers.submit(new Consumer(buffer, histogram, i));
        }

        System.out.println("The program will work for " + numberOfSeconds + " seconds.");
        for (int i = 0; i < numberOfSeconds; i++) {
            Thread.sleep(1000);
            System.out.println("Remaining time: " + (numberOfSeconds - i));
        }

        producersAndConsumers.shutdownNow();
        producersAndConsumers.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        histogram.printValues();
    }


}
