package lab11.example;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

public class Consumer implements CSProcess {
    private final One2OneChannelInt channelIn;
    private final One2OneChannelInt channelReq;
    private final int consumerNumber;
    private int readProducts;

    public Consumer(final One2OneChannelInt channelIn, final One2OneChannelInt channelReq, final int consumerNumber) {
        this.channelIn = channelIn;
        this.channelReq = channelReq;
        this.consumerNumber = consumerNumber;
        this.readProducts = 0;
    }

    public void run() {
        while (true) {
            channelReq.out().write(0);
            int item = channelIn.in().read();
            if (item == -1)
                break;
            readProducts++;
            if (PCMain.showConsumed)
                System.out.println("Consumer " + consumerNumber + " consumed product: " + item);
        }
        System.out.println("\nConsumer " + consumerNumber + " has read " + readProducts + " products and ended.\n");
    }
}
