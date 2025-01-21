// For week 5
package lecture05;

// Model implementation of an AtomicInteger

// Implementation of CAS using a lock for describing its meaning, and
// implementation of AtomicInteger style operations in terms of CAS.

class MyAtomicInteger {
    private int value;    // Visibility ensured by locking

    // Model implementation of compareAndSwap to illustrate its meaning.
    // In practice, compareAndSwap is not implemented using locks; it is
    // compiled to a machine instruction such as CMPXCHG
    public synchronized int compareAndSwap(int oldValue, int newValue) {
        int valueInRegister = this.value;
        if (this.value == oldValue)
            this.value = newValue;
        return valueInRegister;
    }
// wait-free: 在有限步骤内完成
    public synchronized boolean compareAndSet(int oldValue, int newValue) {
        return oldValue == this.compareAndSwap(oldValue,newValue);
    }

    public synchronized int get() {
        return this.value;
    }
    
// lock-free：至少有一个线程完成
// Obstruction-Free：没有干扰时，能够完成操作，有干扰时，可能无限重试
    public int addAndGet(int delta) {
        int oldValue, newValue;
        do {
            oldValue = get();
            newValue = oldValue + delta; // compare old value 和 this.value, line 17
        } while (!compareAndSet(oldValue, newValue));
        return newValue;
    }

    public int getAndAdd(int delta) {
        int oldValue, newValue;
        do {
            oldValue = get();
            newValue = oldValue + delta;
        } while (!compareAndSet(oldValue, newValue));
        return oldValue;
    }

    public int incrementAndGet() {
        return addAndGet(1);
    }

    public int decrementAndGet() {
        return addAndGet(-1);
    }

    public int getAndSet(int newValue) {
        int oldValue;
        do {
            oldValue = get();
        } while (!compareAndSet(oldValue, newValue));
        return oldValue;
    }
}
