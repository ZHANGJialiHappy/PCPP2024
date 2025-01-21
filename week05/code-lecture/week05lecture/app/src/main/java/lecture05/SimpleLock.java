// For week 5
// sestoft@itu.dk * 2014-11-14
package lecture05;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;

// Non-reentrant lock supporting blocking lock and unlock, modified
// from the FIFOMutex example in the Java LockSupport class
// documentation, but using AtomicReference<Thread> instead of
// AtomicBoolean, to check that an unlocking thread owns the lock.
// Based on example in java.util.concurrent.LockSupport documentation

public class SimpleLock {
    // Refers to holding thread, null iff unheld
    private final AtomicReference<Thread> holder = new AtomicReference<Thread>();
    // The FIFO queue of threads waiting for this lock
    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<Thread>();

    public void lock() {
        final Thread current = Thread.currentThread();
        boolean wasInterrupted = false;
        waiters.add(current);
        // Block while not first in queue or cannot acquire lock
        while (waiters.peek() != current || !holder.compareAndSet(null, current)) {
            LockSupport.park(this);
            if (Thread.interrupted())        // also clears interrupted status
                wasInterrupted = true;
        }
        waiters.remove();
        if (wasInterrupted)                // reassert interrupt on exit
            current.interrupt();
    }

    public void unlock() {
        final Thread current = Thread.currentThread();
        if (holder.compareAndSet(current, null))
            LockSupport.unpark(waiters.peek()); // null arg has no effect
        else
            throw new RuntimeException("Not lock holder");
    }
}
