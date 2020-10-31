
class Producer implements Runnable {
    private final Buffer mainBuffer;

    public Producer(Buffer mainBuffer) {
        this.mainBuffer = mainBuffer;
    }

    @Override
    public void run() {
        while (true) {
            int num = Main.generateNumber();
            mainBuffer.put(num);
            Main.producersAccesses.set(num - 1, Main.producersAccesses.get(num - 1) + 1);
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
        }
    }

}