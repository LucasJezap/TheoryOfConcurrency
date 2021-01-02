package lab11.ordered;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

public class Producer implements CSProcess {
    private final One2OneChannelInt channelOut;
    private final int numberOfProductsToWrite;
    private final Consumer consumer;

    public Producer(final One2OneChannelInt channelOut, final int numberOfProductsToWrite, final Consumer consumer) {
        this.channelOut = channelOut;
        this.numberOfProductsToWrite = numberOfProductsToWrite;
        this.consumer = consumer;
    }

    /**
     * producer produces N products
     * it always write a new product to buffer[0]
     * it waits if there's still a product in buffer[0]
     */
    public void run() {
        final long start = System.nanoTime();

        for (int i = 0; i < numberOfProductsToWrite; i++) {
            int product = (int) (Math.random() * 100) + 1;
            if (PCMain3.showProducerInfo)
                System.out.println("Producer created product: the product is " + product);
            // blocks until buffer[0] free
            channelOut.out().write(product);
        }

        final long end = System.nanoTime();
        System.out.println("Producer has ended.\nTime (in nanoseconds): " + (end - start));
        consumer.producerFinished = true;
    }
}
