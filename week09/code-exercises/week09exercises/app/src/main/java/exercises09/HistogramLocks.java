// jst@itu.dk * 04/10/2023
package exercises09;

class HistogramLocks implements Histogram {
  private final int[] counts;
  private int total= 0;
    /* Invariant
       total = counts[0] + counts[1] +  ... + counts[counts.length-1]
    */

  public HistogramLocks(int span) {
		this.counts= new int[span]; // the elements of an array are all initialized to 0
  }

  public synchronized void increment(int bin) {
		counts[bin]= counts[bin] + 1;
		total= total+1;
  }

  public int getCount(int bin) {
		return counts[bin];
  }
    
  public synchronized float getPercentage(int bin){
		return getCount(bin) / getTotal() * 100;
  }

  public int getSpan() {
		return counts.length;
  }
    
  public int getTotal(){
		return total;
  }

  // This method is new to comply with the interface
  public synchronized int getAndClear(int bin){
    int result  = getCount(bin);
    counts[bin] = 0;
    return result;
  }
}
