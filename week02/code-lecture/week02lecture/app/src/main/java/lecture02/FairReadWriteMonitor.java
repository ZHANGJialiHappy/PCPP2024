// For week 2
// raup@itu.dk * 01/09/2021
package lecture02;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class FairReadWriteMonitor {
    private int readsAcquires   = 0;
    private int readsReleases   = 0;
    private boolean writer      = false;
    private Lock lock           = new ReentrantLock();
    private Condition condition = lock.newCondition();

    //////////////////////////
    // Read lock operations // 确保当前线程在写入共享资源时没有任何其他线程正在写入。
    //////////////////////////

    public void readLock() {
        lock.lock();
        try {
            while(writer)
                condition.await();
            readsAcquires++;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    // public void writeLock() {
    //     lock.lock();
    //     try {
    //         while(writer)
    //             condition.await();
    //         writer=true;
    //         while(readsAcquires != readsReleases)
    //             condition.await();
    //     }
    //     catch (InterruptedException e) {
    //         e.printStackTrace();
    //     }
    //     finally {
    //         lock.unlock();
    //     }
    // }

    public void readUnlock() {
        lock.lock();
        try {
            readsReleases++;
            if(readsAcquires==readsReleases)
                condition.signalAll();
        }
        finally {
            lock.unlock();
        }
    }


    ///////////////////////////
    // Write lock operations // 确保当前线程在写入共享资源时没有任何其他线程正在读取或写入。
    ///////////////////////////

    public void writeLock() {
        lock.lock();
        try {
            while(writer)
                condition.await();
            writer=true;
            while(readsAcquires != readsReleases)
                condition.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    public void writeUnlock() {
        lock.lock();
        try {
            writer=false;
            condition.signalAll();
        }
        finally {
            lock.unlock();
        }
    }

}
