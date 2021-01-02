package lab11.ordered;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

public class Buffer implements CSProcess {
    private final One2OneChannelInt channelIn, channelOut;

    public Buffer(final One2OneChannelInt channelIn, final One2OneChannelInt channelOut) {
        this.channelIn = channelIn;
        this.channelOut = channelOut;
    }

    /**
     * every buffer cell works infinitely
     * it writes value read from channelIn to channelOut (stores a product)
     * stricly speaking, it writes value from buffer[i] to buffer[i+1]
     * producer is at buffer[0] while consumer is at buffer[N]
     *
     */
    public void run() {
        while (true) {
            var outOut = channelOut.out();
            var inIn = channelIn.in();
            outOut.write(inIn.read());
        }
    }
}
