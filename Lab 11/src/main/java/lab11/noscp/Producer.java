package lab11.noscp;

public class Producer extends Thread {
    private final Buffer buffer;
    private final int numberOfProductsToWrite;
    private final Consumer consumer;

    public Producer(final Buffer buffer, final int numberOfProductsToWrite, final Consumer consumer) {
        this.buffer = buffer;
        this.numberOfProductsToWrite = numberOfProductsToWrite;
        this.consumer = consumer;
    }

    /**
     * Producing N products using Buffer's method put
     */
    @Override
    public void run() {
        final long start = System.nanoTime();

        for (int i = 0; i < numberOfProductsToWrite; i++) {
            int product = (int) (Math.random() * 100) + 1;
            if (PCMain4.showProducerInfo)
                System.out.println("Producer created product: the product is " + product);
            buffer.put(product);
        }

        final long end = System.nanoTime();
        System.out.println("Producer has ended.\nTime (in nanoseconds): " + (end - start));
        consumer.producerFinished = true;
    }
}
