
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Buffer {
    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    final Lock producerLock = new ReentrantLock(true);
    final Lock consumerLock = new ReentrantLock(true);

    int bufferValue;
    final int bufferMaxSize;

    Buffer(int bufferStartValue, int bufferMaxSize) {
        this.bufferValue = bufferStartValue;
        this.bufferMaxSize = bufferMaxSize;
    }

    public void put(int value) {
        if (Main.isFair)
            producerLock.lock();
        lock.lock();
        try {
            while (bufferValue + value > bufferMaxSize) {
                try {
                    notFull.await();
                } catch (InterruptedException e) {
                    if (Main.isFair)
                        producerLock.unlock();
                    lock.unlock();
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            bufferValue += value;
            notEmpty.signalAll();
        } finally {
            if (Main.isFair)
                producerLock.unlock();
            lock.unlock();
        }

    }

    public void take(int value) {
        if (Main.isFair)
            consumerLock.lock();
        lock.lock();
        try {
            while (bufferValue - value < 0) {
                try {
                    notEmpty.await();
                } catch (InterruptedException e) {
                    if (Main.isFair)
                        consumerLock.unlock();
                    lock.unlock();
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            bufferValue -= value;
            notFull.signalAll();
        } finally {
            if (Main.isFair)
                consumerLock.unlock();
            lock.unlock();
        }

    }
}
