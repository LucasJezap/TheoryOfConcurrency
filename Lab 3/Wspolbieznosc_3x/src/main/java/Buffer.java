
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Buffer {
    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    int bufferValue;
    final int bufferMaxSize;

    Buffer(int bufferStartValue, int bufferMaxSize) {
        this.bufferValue = bufferStartValue;
        this.bufferMaxSize = bufferMaxSize;
    }

    public void put(int value) {
        lock.lock();
        try {
            while (bufferValue + value > bufferMaxSize) {
                try {
                    notFull.await();
                } catch (InterruptedException e) {
                    lock.unlock();
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            bufferValue += value;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }

    }

    public void take(int value){
        lock.lock();
        try {
            while (bufferValue - value < 0) {
                try {
                    notEmpty.await();
                } catch (InterruptedException e) {
                    lock.unlock();
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            bufferValue -= value;
            notFull.signalAll();
        } finally {
            lock.unlock();
        }

    }
}
