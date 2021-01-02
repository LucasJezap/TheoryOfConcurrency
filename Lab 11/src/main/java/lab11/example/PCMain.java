package lab11.example;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;

import java.util.ArrayList;

public final class PCMain {

    final private static int numOfProducers = 5;
    final private static int numOfConsumers = 8;
    final private static int numberOfProducts = 10;
    final static int N = 10;
    final static boolean showProduced = false;
    final static boolean showConsumed = false;


    public static void main(String[] args) {
        new PCMain();
    }

    public PCMain() {
        ArrayList<One2OneChannelInt> channelOut = new ArrayList<>();
        ArrayList<One2OneChannelInt> channelReq = new ArrayList<>();
        ArrayList<One2OneChannelInt> channelIn = new ArrayList<>();
        CSProcess[] procList = new CSProcess[numOfConsumers + numOfProducers + 1];

        for (int i = 0; i < numOfProducers; i++) {
            channelOut.add(Channel.one2oneInt());
        }
        for (int i = 0; i < numOfConsumers; i++) {
            channelReq.add(Channel.one2oneInt());
            channelIn.add(Channel.one2oneInt());
        }

        int index = 0;
        for (int i = 0; i < numOfProducers; i++) {
            procList[index++] = new Producer(channelOut.get(i), i + 1, numberOfProducts);
        }

        procList[index++] = new Buffer(channelOut, channelReq, channelIn, numOfProducers, numOfConsumers);

        for (int i = 0; i < numOfConsumers; i++) {
            procList[index++] = new Consumer(channelIn.get(i), channelReq.get(i), i + 1);
        }

        Parallel parallel = new Parallel(procList);
        parallel.run();
    }
}
