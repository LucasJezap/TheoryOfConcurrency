package lab11.example;

import org.jcsp.lang.*;

public class Producer implements CSProcess {
    private final One2OneChannelInt channelOut;
    private final int producerNumber;
    private final int numberOfProducts;

    public Producer(final One2OneChannelInt channelOut, final int producerNumber, final int numberOfProducts) {
        this.channelOut = channelOut;
        this.producerNumber = producerNumber;
        this.numberOfProducts = numberOfProducts;
    }

    public void run() {
        for (int i = 0; i < numberOfProducts; i++) {
            int item = (int) (Math.random() * 100) + 1;
            channelOut.out().write(item);
            if (PCMain.showProduced)
                System.out.println("Producer " + producerNumber + " produced product: " + item);
        }
        channelOut.out().write(-1);
        System.out.println("\nProducer " + producerNumber + " has produced " + numberOfProducts + " products.\n");
    }
}
