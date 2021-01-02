package lab11.ordered;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

public class Consumer implements CSProcess {
    private final One2OneChannelInt channelIn;
    private final int numberOfProductsToRead;
    volatile boolean producerFinished = false;

    public Consumer(final One2OneChannelInt channelIn, final int numberOfProductsToRead) {
        this.channelIn = channelIn;
        this.numberOfProductsToRead = numberOfProductsToRead;
    }

    /**
     * consumer consumes N products
     * it always read a product from buffer[N]
     * it waits if there's no product in buffer[N]
     */
    public void run() {
        final long start = System.nanoTime();

        for (int i = 0; i < numberOfProductsToRead; i++) {
            // waits until product available
            int product = channelIn.in().read();
            if (PCMain3.showConsumerInfo)
                System.out.println("Consumer read product: the product is " + product);
        }

        final long end = System.nanoTime();

        while (!producerFinished) Thread.onSpinWait();

        System.out.println("Consumer has ended.\nTime (in nanoseconds): " + (end - start));
        System.exit(0);
    }
}
