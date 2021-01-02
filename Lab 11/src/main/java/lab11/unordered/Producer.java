package lab11.unordered;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

import java.util.ArrayList;

public class Producer implements CSProcess {
    private final ArrayList<One2OneChannelInt> channelOut;
    private final ArrayList<One2OneChannelInt> channelAvailable;
    private final int numberOfProductsToWrite;
    private final Consumer consumer;

    public Producer(final ArrayList<One2OneChannelInt> channelOut, final ArrayList<One2OneChannelInt> channelAvailable,
                    final int numberOfProductsToWrite, final Consumer consumer) {
        this.channelOut = channelOut;
        this.channelAvailable = channelAvailable;
        this.numberOfProductsToWrite = numberOfProductsToWrite;
        this.consumer = consumer;
    }

    /**
     * producer produces N products
     * it uses Alternative object to determine which buffer cell should be used to store a product
     * it uses Guards with appropriate channelAvailable channels to guess the free cell
     */
    public void run() {
        final long start = System.nanoTime();
        final Guard[] guards = new Guard[channelAvailable.size()];
        for (int i = 0; i < channelAvailable.size(); i++) {
            guards[i] = channelAvailable.get(i).in();
        }

        final Alternative alternative = new Alternative(guards);

        for (int i = 0; i < numberOfProductsToWrite; i++) {
            int index = alternative.select();
            // blocks until available
            channelAvailable.get(index).in().read();
            int product = (int) (Math.random() * 100) + 1;
            if (PCMain2.showProducerInfo)
                System.out.println("Producer created product on buffer index " + index + ": the product is " + product);
            channelOut.get(index).out().write(product);
        }

        final long end = System.nanoTime();
        System.out.println("Producer has ended.\nTime (in nanoseconds): " + (end - start));

        consumer.producerFinished = true;
    }
}
