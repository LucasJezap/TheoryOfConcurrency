package lab11.noscp;

public class Consumer extends Thread {
    private final Buffer buffer;
    private final int numberOfProductsToRead;
    volatile boolean producerFinished = false;

    public Consumer(final Buffer buffer, final int numberOfProductsToRead) {
        this.buffer = buffer;
        this.numberOfProductsToRead = numberOfProductsToRead;
    }

    /**
     * Consuming N products using Buffer's method get
     */
    @Override
    public void run() {
        final long start = System.nanoTime();

        for (int i = 0; i < numberOfProductsToRead; i++) {
            int product = buffer.get();
            if (PCMain4.showConsumerInfo)
                System.out.println("Consumer read product: the product is " + product);
        }

        final long end = System.nanoTime();

        while (!producerFinished) Thread.onSpinWait();

        System.out.println("Consumer has ended.\nTime (in nanoseconds): " + (end - start));
    }
}
