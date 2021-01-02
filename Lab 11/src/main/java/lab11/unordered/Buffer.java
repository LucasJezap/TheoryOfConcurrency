package lab11.unordered;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

public class Buffer implements CSProcess {
    private final One2OneChannelInt channelIn, channelOut, channelAvailable;

    public Buffer(final One2OneChannelInt channelIn, final One2OneChannelInt channelOut, final One2OneChannelInt channelAvailable) {
        this.channelIn = channelIn;
        this.channelOut = channelOut;
        this.channelAvailable = channelAvailable;
    }

    /**
     * every buffer cell works infinitely
     * it uses channelAvailable channel to checks whether it's free
     * if so, it writes value read from channelIn to channelOut (stores a product)
     * stricly speaking, it writes a value from buffer[i] to buffer[i+1]
     * producer is at buffer[0] while consumer is at buffer[N]
     * it preserves ordering - FIFO
     */
    public void run() {
        while (true) {
            var availableOut = channelAvailable.out();
            var outOut = channelOut.out();
            var inIn = channelIn.in();
            availableOut.write(0);
            outOut.write(inIn.read());
        }
    }
}
