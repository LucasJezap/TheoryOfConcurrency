
class Consumer implements Runnable {
    private final Buffer mainBuffer;

    public Consumer(Buffer mainBuffer) {
        this.mainBuffer = mainBuffer;
    }

    @Override
    public void run() {
        while (true) {
            int num = Main.generateNumber();
            mainBuffer.take(num);
            Main.consumersAccesses.set(num - 1, Main.consumersAccesses.get(num - 1) + 1);
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
        }
    }
}
