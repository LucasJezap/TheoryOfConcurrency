
import java.util.Random;

class Consumer implements Runnable {
    private final Buffer mainBuffer;
    private final Histogram histogram;
    private final int consumerID;
    private final int consumeSize;
    private int bufferAccesses;

    public Consumer(Buffer mainBuffer, Histogram histogram, int consumerID) {
        this.mainBuffer = mainBuffer;
        this.histogram = histogram;
        this.consumeSize = new Random().nextInt((int) Math.floor(mainBuffer.bufferMaxSize / 2.0)) + 1;
        this.bufferAccesses = 0;
        this.consumerID = consumerID;
    }

    @Override
    public void run() {
        while (true) {
            mainBuffer.take(consumeSize);
            bufferAccesses++;
            histogram.setValue(false, consumerID, consumeSize, bufferAccesses);
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
        }
    }
}
