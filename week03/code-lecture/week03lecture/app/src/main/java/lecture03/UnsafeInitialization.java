// Week 3
// raup@itu.dk * 19/09/2021
package lecture03;

import java.util.Objects;

class TestUnsafeInitialization {
    public TestUnsafeInitialization() {
        int N = 10_000_000;
        for (int i = 0; i < N; i++) {

            UnsafeInitialization u = new UnsafeInitialization();

            new Thread(() -> {
                    if (u.readX()!=42)
                        System.out.println("x is not equal 42");
            }).start();

            new Thread(() -> {
                    if (Objects.isNull(u.readO()))
                        System.out.println("o is null");
            }).start();
        }
    }

    public static void main(String[] args) {
        new TestUnsafeInitialization();
    }
}


class UnsafeInitialization {
    private int x; //x 是 volatile 时，读线程仍可能在构造函数完成之前访问 x，因此仍有小概率打印 x is not equal 42，尤其是在高并发情况下。
    private Object o; // 如果 o 是 final，这种在构造前被读取的可能性为 0，因为 final 保证了构造结束时 o 的安全发布。

    public UnsafeInitialization() {
        try {
            Thread.sleep(1000); // 暂停 1 秒 (1000 毫秒)
        } catch (InterruptedException e) {
            System.out.println("睡眠被中断！");
        }
        this.x = 42;
        this.o = new Object();
    }

    public int readX() {
        return this.x;
    }

    public Object readO() {
        return this.o;
    }
}
