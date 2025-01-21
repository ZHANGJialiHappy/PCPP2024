package exercises09;

// first version by Kasper modified by jst@itu.dk 24-09-2021
// raup@itu.dk * 05/10/2022
// jst@itu.dk * 04/10/2023

interface Histogram {
  public void increment(int bin);
  public int getCount(int bin);
  public int getSpan();
}
