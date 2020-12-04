package arbitrator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Table {
    private final int numberOfForks;
    final ArrayList<Semaphore> forks;
    final Semaphore arbitrator;


    Table(int numberOfForks) {
        this.numberOfForks = numberOfForks;
        this.forks = new ArrayList<>();
        for (int i=0; i<numberOfForks; i++)
            forks.add(new Semaphore(1));
        arbitrator = new Semaphore(numberOfForks - 1);
    }

    public void put(int philosopherIndex) throws InterruptedException {
        arbitrator.acquire();
        forks.get(philosopherIndex).acquire();
        forks.get((philosopherIndex + 1) % numberOfForks).acquire();
        //System.out.println("Philosopher " + philosopherIndex + ": eating");
    }

    public void take(int philosopherIndex){
        //System.out.println("Philosopher " + philosopherIndex + ": stopped eating");
        forks.get(philosopherIndex).release();
        forks.get((philosopherIndex + 1) % numberOfForks).release();
        arbitrator.release();
    }
}
