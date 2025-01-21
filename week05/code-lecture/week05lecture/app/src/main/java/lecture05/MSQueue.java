// For week 5
// sestoft@itu.dk * 2014-11-16

// Unbounded list-based lock-free queue by Michael and Scott 1996 (who
// calls it non-blocking).
package lecture05;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

// ------------------------------------------------------------
// Unbounded lock-free queue (non-blocking in M&S terminology),
// using CAS and AtomicReference

// This creates one AtomicReference object for each Node object.  The
// MSQueueRefl class uses one-time reflection to create an
// AtomicReferenceFieldUpdater, thereby avoiding this extra object.
// In practice the overhead of the extra object apparently does not
// matter much.

class MSQueue<T> implements UnboundedQueue<T> {
    private final AtomicReference<Node<T>> head, tail;

    public MSQueue() {
        Node<T> dummy = new Node<T>(null, null);
        head = new AtomicReference<Node<T>>(dummy);
        tail = new AtomicReference<Node<T>>(dummy);
    }

    public void enqueue(T item) { // at tail
        Node<T> node = new Node<T>(item, null);
        while (true) {
            Node<T> last = tail.get(), next = last.next.get();
            if (last == tail.get()) {         // E7 ：2. 其他线程update tail，失败
                if (next == null)  { //E8 ：3. 其他线程update next，但是没有update tail，于是跳转到 else
                    // In quiescent state, try inserting new node
                    if (last.next.compareAndSet(next, node)) { // E9 1. linearization point :只有一个线程成功
                        // Insertion succeeded, try advancing tail
                        tail.compareAndSet(last, node);
                        return;
                    }
                } else
                    // Queue in intermediate state, advance tail
                    tail.compareAndSet(last, next); // another thread is enqueuing, and didn’t update the tail, the current thread helps by advancing the tail
            }
        }
    }

    public T dequeue() { // from head
        while (true) {
            Node<T> first = head.get(), last = tail.get(), next = first.next.get(); // D3 1. linearization point
            if (first == head.get()) {        // D5 2. 其他线程update head，失败
                if (first == last) {
                    if (next == null)
                        return null;
                    else
                        tail.compareAndSet(last, next);//可能有其他线程enqueue
                } else {
                    T result = next.item;
                    if (head.compareAndSet(first, next)) // D13 1. linearization point ，只有一个thread can update成功
                        return result;
                }
            }
        }
    }

    private static class Node<T> {
        final T item;
        final AtomicReference<Node<T>> next;

        public Node(T item, Node<T> next) {
            this.item = item;
            this.next = new AtomicReference<Node<T>>(next);
        }
    }
}
