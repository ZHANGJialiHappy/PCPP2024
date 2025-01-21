package exercises09;
// For week 9
// raup@itu.dk * 10/10/2021
// jst@itu.dk * 04/10/2024

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntToDoubleFunction;
import benchmarking.Benchmark;

class TestCASLockHistogram {
  public static void main(String[] args) {

    // Create an object `histogramCAS` with your Histogram CAS implementation from week05
    // Create an object `histogramLock` using the class in the file HistogramLocks
    
    int noThreads= 16;
    int range= 100_000;	

    for (int i= 1; i < noThreads; i++) {
      int threadCount= i;
      // Benchmark.Mark7( ... using a monitor
    }

    for (int i= 1; i < noThreads; i++) {
      int threadCount= i;
      // Benchmark.Mark7( ... using CAS
    }
  }
  
  // Function to count the prime factors of a number `p`
  private static int countFactors(int p) {
    if (p < 2) return 0;
    int factorCount = 1, k = 2;
    while (p >= k * k) {
      if (p % k == 0) {
        factorCount++;
        p= p/k;
      } else 
        k= k+1;
    }
    return factorCount;
  }

  // Parallel execution of counting the number of primes for numbers in `range`
    private static void countParallel(int range, int threadCount, Histogram h) {
    final int perThread= range / threadCount;
    Thread[] threads= new Thread[threadCount];
    for (int t=0; t<threadCount; t= t+1) {
      final int from= perThread * t, 
        to= (t+1==threadCount) ? range : perThread * (t+1); 
      threads[t]= new Thread( () -> {
          for (int i= from; i<to; i++) h.increment(countFactors(i));
                
      });
    } 
    for (int t= 0; t<threadCount; t= t+1) 
      threads[t].start();
    try {
      for (int t= 0; t<threadCount; t= t+1) 
        threads[t].join();
    } catch (InterruptedException exn) { }
  }
    
  // Auxiliary method to print the histogram data
  public static void dump(Histogram histogram) {
    for (int bin= 0; bin < histogram.getSpan(); bin= bin+1) {
      System.out.printf("%4d: %9d%n", bin, histogram.getCount(bin));
    }
  }
}


