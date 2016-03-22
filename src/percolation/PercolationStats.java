package percolation;

import edu.princeton.cs.algs4.StdRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liww.li
 * @date 2016-03-22 11:03
 */
public class PercolationStats {
    private Percolation percolation;
    private int time;
    private int N;
    private double mean;
    private double stddev;
    private double lo;
    private double hi;
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException();
        time = T;
        this.N = N;
    }    // perform T independent experiments on an N-by-N grid

    private void performPercolation() {
        List<Double> xList = new ArrayList<>();
        for (int i = 0; i < time; i++) {
            percolation = new Percolation(N);
            int opend = 0;
            List<Integer> location = allLocation();
            while (!percolation.percolates()) {
                int rnd = StdRandom.uniform(0, location.size());
                int loc = location.get(rnd);
                percolation.open(loc / N + (((loc % N) == 0) ? 0 : 1), (loc % N == 0) ? N : loc % N);
                location.remove(rnd);
                opend++;
            }
            xList.add((double)opend/(N*N));
        }
        double sum = 0.0;
        double stdsum = 0.0;
        for (double x : xList) {
            sum += x;
        }
        mean = sum / time;
        for (double x : xList) {
            stdsum += Math.pow((x - mean), 2);
        }
        stddev = mean / (time - 1);
        double tmp = 1.96 * Math.sqrt(stddev) / Math.sqrt(time);
        lo = mean - tmp;
        hi = mean + tmp;
    }

    private List<Integer> allLocation() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= N*N; i++) {
            list.add(i);
        }
        return list;
    }

    public double mean()  {
        return mean;
    }                    // sample mean of percolation threshold
    public double stddev() {
        return stddev;
    }                    // sample standard deviation of percolation threshold
    public double confidenceLo() {
        return lo;
    }              // low  endpoint of 95% confidence interval
    public double confidenceHi() {
        return hi;
    }             // high endpoint of 95% confidence interval

    public static void main(String[] args) {
        if (args.length != 2)
            System.out.println("Usage: java PercolationStats N T");
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(N,T);
        percolationStats.performPercolation();
        System.out.printf("%-20s = %f%n", "mean", percolationStats.mean());
        System.out.printf("%-20s = %f%n", "stddev", percolationStats.stddev());
        System.out.printf("95%% confidence interval = %f, %f%n",
                percolationStats.confidenceLo(), percolationStats.confidenceHi());

    }   // test client (described below)
}

