package arbitrator;

class Philosopher implements Runnable {
    private final Table table;
    private final int philosopherIndex;
    private int accesses;
    private boolean interrupted;

    public Philosopher(Table table, final int philosopherIndex) {
        this.table = table;
        this.philosopherIndex = philosopherIndex;
        this.accesses = 0;
        this.interrupted = false;
    }

    private void think() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            interrupted = true;
        }
    }

    private void eat(){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            interrupted = true;
        }
    }

    @Override
    public void run() {
        while (true) {
            think();
            try {
                table.put(philosopherIndex);
            } catch (InterruptedException e) {
                interrupted = true;
            }
            eat();
            Main.accesses.set(philosopherIndex, Main.accesses.get(philosopherIndex) + 1);
            table.take(philosopherIndex);

            if (interrupted) {
                return;
            }
        }
    }

}