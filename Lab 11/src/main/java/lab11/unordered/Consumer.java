package lab11.unordered;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

import java.util.ArrayList;

public class Consumer implements CSProcess {
    private final ArrayList<One2OneChannelInt> channelIn;
    private final int numberOfProductsToRead;
    volatile boolean producerFinished = false;

    public Consumer(final ArrayList<One2OneChannelInt> channelIn, final int numberOfProductsToRead) {
        this.channelIn = channelIn;
        this.numberOfProductsToRead = numberOfProductsToRead;
    }

    /**
     * consumer consumes N products
     * it uses Alternative object to determine which buffer cell should be used to read from
     * it uses Guards with appropriate channelAvailable channels to guess the free cell
     */
    public void run() {
        final long start = System.nanoTime();

        final Guard[] guards = new Guard[channelIn.size()];
        for (int i = 0; i < channelIn.size(); i++) {
            guards[i] = channelIn.get(i).in();
        }

        final Alternative alternative = new Alternative(guards);

        for (int i = 0; i < numberOfProductsToRead; i++) {
            int index = alternative.select();
            // blocks until available
            int product = channelIn.get(index).in().read();
            if (PCMain2.showConsumerInfo)
                System.out.println("Consumer read index " + index + ": the product is " + product);
        }

        final long end = System.nanoTime();

        while (!producerFinished) Thread.onSpinWait();

        System.out.println("Consumer has ended.\nTime (in nanoseconds): " + (end - start));
        System.exit(0);
    }
}
