package lab11.ordered;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;

import java.util.ArrayList;

public final class PCMain3 {

    final private static int numberOfProducts = 100000;
    final static int N = 10;
    final static boolean showProducerInfo = false;
    final static boolean showConsumerInfo = false;

    public static void main(String[] args) {
        new PCMain3();
    }

    /**
     * this method is slower than unordered one
     * it's so because it must transfer product from buffer[0] to buffer[N]
     * while in unordered method it does not use such tricks - the number of transfer there is 2
     */
    public PCMain3() {
        System.out.println("Producer/Consumer: ORDERED version");
        ArrayList<One2OneChannelInt> channel = new ArrayList<>();

        for (int i = 0; i < N + 1; i++) {
            channel.add(Channel.one2oneInt());
        }
        CSProcess[] procList = new CSProcess[N + 2];

        // CS Processes: 1 Producer, 1 Consumer, N buffer processes
        var consumer = new Consumer(channel.get(N), numberOfProducts);
        procList[0] = new Producer(channel.get(0), numberOfProducts, consumer);
        procList[1] = consumer;
        for (int i = 0; i < N; i++) {
            procList[2 + i] = new Buffer(channel.get(i), channel.get(i + 1));
        }

        Parallel parallel = new Parallel(procList);
        parallel.run();
    }
}
