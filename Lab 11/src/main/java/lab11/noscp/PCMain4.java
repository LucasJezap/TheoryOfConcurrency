package lab11.noscp;

public final class PCMain4 {

    final private static int numberOfProducts = 100000;
    final static int N = 200;
    final static boolean showProducerInfo = false;
    final static boolean showConsumerInfo = false;

    public static void main(String[] args) {
        System.out.println("Producer/Consumer: NO-CSP version");
        Buffer buffer = new Buffer(N);
        Consumer consumer = new Consumer(buffer, numberOfProducts);
        Producer producer = new Producer(buffer, numberOfProducts, consumer);

        producer.start();
        consumer.start();
    }

}
