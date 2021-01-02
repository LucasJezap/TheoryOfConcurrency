package lab11.unordered;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;

import java.util.ArrayList;

public final class PCMain2 {

    final private static int numberOfProducts = 100000;
    final static int N = 10;
    final static boolean showProducerInfo = false;
    final static boolean showConsumerInfo = false;

    public static void main(String[] args) {
        new PCMain2();
    }

    /**
     * this method is faster than ordered one
     * it's so because ordered version must transfer product from buffer[0] to buffer[N]
     * while in this method it does not use such tricks - the number of transfer here is 2
     */
    public PCMain2() {
        System.out.println("Producer/Consumer: UNORDERED version");
        ArrayList<One2OneChannelInt> channelOut = new ArrayList<>();
        ArrayList<One2OneChannelInt> channelAvailable = new ArrayList<>();
        ArrayList<One2OneChannelInt> channelIn = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            channelOut.add(Channel.one2oneInt());
            channelAvailable.add(Channel.one2oneInt());
            channelIn.add(Channel.one2oneInt());
        }
        CSProcess[] procList = new CSProcess[N + 2];

        // CS Processes: 1 Producer, 1 Consumer, N buffer processes
        var consumer = new Consumer(channelIn, numberOfProducts);
        procList[0] = new Producer(channelOut, channelAvailable, numberOfProducts, consumer);
        procList[1] = consumer;
        for (int i = 0; i < N; i++) {
            procList[2 + i] = new Buffer(channelOut.get(i), channelIn.get(i), channelAvailable.get(i));
        }

        Parallel parallel = new Parallel(procList);
        parallel.run();
    }
}
