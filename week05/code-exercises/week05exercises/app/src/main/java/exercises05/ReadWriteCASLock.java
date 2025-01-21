// For week 5
// raup@itu.dk * 10/10/2021
// raup@itu.dk * 2024-09-22

package exercises05;

// Very likely you will need some imports here

class ReadWriteCASLock implements SimpleRWTryLockInterface {

    // TODO: Add necessary field(s) for the class

    public boolean readerTryLock() {
        // TODO 5.2.3
        return true;
    }

    public void readerUnlock() {
        // TODO 5.2.4
    }

    public boolean writerTryLock() {
        // TODO 5.2.1
        return true;
    }

    public void writerUnlock() {
        // TODO 5.2.2
    }


    // Challenging 5.2.7: You may add new methods




    private static abstract class Holders { }

    private static class ReaderList extends Holders {
        private final Thread thread;
        private final ReaderList next;

        // TODO: Constructor

        // TODO: contains

        // TODO: remove
    }

    private static class Writer extends Holders {
        public final Thread thread;

        // TODO: Constructor

    }
}
