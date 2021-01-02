package lab11.example;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

import java.util.ArrayList;

public class Buffer implements CSProcess {
    private final ArrayList<One2OneChannelInt> channelIn;
    private final ArrayList<One2OneChannelInt> channelReq;
    private final ArrayList<One2OneChannelInt> channelOut;
    private final int numberOfProducers;
    private final int numberOfConsumers;
    private final int N = PCMain.N;
    private final int[] buffer = new int[N];
    int head = -1;
    int tail = -1;

    public Buffer(final ArrayList<One2OneChannelInt> channelIn, final ArrayList<One2OneChannelInt> channelReq,
                  final ArrayList<One2OneChannelInt> channelOut, final int numberOfProducers, final int numberOfConsumers) {
        this.channelIn = channelIn;
        this.channelReq = channelReq;
        this.channelOut = channelOut;
        this.numberOfProducers = numberOfProducers;
        this.numberOfConsumers = numberOfConsumers;
    }

    public void run() {
        final Guard[] guards = new Guard[numberOfProducers + numberOfConsumers];
        int idx = 0;
        for (int i = 0; i < numberOfProducers; i++) {
            guards[idx++] = channelIn.get(i).in();
        }
        for (int i = 0; i < numberOfConsumers; i++) {
            guards[idx++] = channelReq.get(i).in();
        }
        final Alternative alternative = new Alternative(guards);
        int countdown = numberOfConsumers + numberOfProducers;
        while (countdown > 0) {
            int index = alternative.select();
            if (index < numberOfProducers) {
                if (head <= tail + N) {
                    int item = channelIn.get(index).in().read();
                    if (item == -1)
                        countdown--;
                    else {
                        head++;
                        buffer[head % buffer.length] = item;
                    }
                }
            } else {
                if (tail < head) {
                    channelReq.get(index - numberOfProducers).in().read();
                    tail++;
                    int item = buffer[tail % buffer.length];
                    channelOut.get(index - numberOfProducers).out().write(item);
                } else if (countdown <= numberOfConsumers) {
                    channelReq.get(index - numberOfProducers).in().read();
                    channelOut.get(index - numberOfProducers).out().write(-1);
                    countdown--;
                }
            }
        }
        System.out.println("The buffer has ended.");
    }
}
