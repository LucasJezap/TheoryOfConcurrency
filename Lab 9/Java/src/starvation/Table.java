package starvation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Table {
    private final int numberOfForks;
    private final Lock lock = new ReentrantLock();
    private final ArrayList<Integer> freeForks;
    private final ArrayList<Condition> philosophers;


    Table(int numberOfForks) {
        this.numberOfForks = numberOfForks;
        this.freeForks = new ArrayList<>(Collections.nCopies(numberOfForks, 2));
        this.philosophers = new ArrayList<>();
        for (int i = 0; i < numberOfForks; i++)
            philosophers.add(lock.newCondition());
    }

    public void put(int philosopherIndex) {
        lock.lock();
        while (freeForks.get(philosopherIndex) < 2) {
            try {
                philosophers.get(philosopherIndex).await();
            } catch (InterruptedException e) {
                lock.unlock();
                Thread.currentThread().interrupt();
                return;
            }
        }
        freeForks.set((philosopherIndex + numberOfForks - 1) % numberOfForks,
                freeForks.get((philosopherIndex + numberOfForks - 1) % numberOfForks) - 1);
        freeForks.set((philosopherIndex + 1) % numberOfForks,
                freeForks.get((philosopherIndex + 1) % numberOfForks) - 1);
        lock.unlock();
    }

    public void take(int philosopherIndex) {
        lock.lock();
        freeForks.set((philosopherIndex + numberOfForks - 1) % numberOfForks,
                freeForks.get((philosopherIndex + numberOfForks - 1) % numberOfForks) + 1);
        freeForks.set((philosopherIndex + 1) % numberOfForks,
                freeForks.get((philosopherIndex + 1) % numberOfForks) + 1);
        philosophers.get((philosopherIndex + numberOfForks - 1) % numberOfForks).signal();
        philosophers.get((philosopherIndex + 1) % numberOfForks).signal();
        lock.unlock();
    }
}
