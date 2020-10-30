
import java.util.Random;

class Producer implements Runnable {
    private final Buffer mainBuffer;
    private final Histogram histogram;
    private final int producerID;
    private final int produceSize;
    private int bufferAccesses;

    public Producer(Buffer mainBuffer, Histogram histogram, int producerID) {
        this.mainBuffer = mainBuffer;
        this.histogram = histogram;
        this.produceSize = new Random().nextInt((int) Math.floor(mainBuffer.bufferMaxSize / 2.0)) + 1;
        this.bufferAccesses = 0;
        this.producerID = producerID;
    }

    @Override
    public void run() {
        while (true) {
            mainBuffer.put(produceSize);
            bufferAccesses++;
            histogram.setValue(true, producerID, produceSize, bufferAccesses);
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
        }
    }

}