import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

import java.util.ArrayList;
import java.util.List;

public class PercolationStats {
    private int N, T;
    double[] xList;
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException();
        this.N = N;
        this.T = T;
        xList = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation percolation = new Percolation(N);
            int opend = 0;
            List<Integer> location = allLocation();
            while (!percolation.percolates()) {
                int rnd = StdRandom.uniform(0, location.size());
                int loc = location.get(rnd);
                int x, y;
                if (loc % N == 0) {
                    x = loc / N;
                    y = N;
                } else {
                    x = loc / N + 1;
                    y = loc % N;
                }
                percolation.open(x, y);
                location.remove(rnd);
                opend++;
            }
            xList[i] = ((double) opend/(N*N));
        }
    }    // perform T independent experiments on an N-by-N grid

    private List<Integer> allLocation() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= N*N; i++) {
            list.add(i);
        }
        return list;
    }

    public double mean()  {
        return StdStats.mean(xList);
    }                    // sample mean of percolation threshold
    public double stddev() {
        return StdStats.stddev(xList);
    }                    // sample standard deviation of percolation threshold
    public double confidenceLo() {
        return mean() - Math.sqrt(stddev()) / Math.sqrt(T);
    }              // low  endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + Math.sqrt(stddev()) / Math.sqrt(T);
    }             // high endpoint of 95% confidence interval

    public static void main(String[] args) {
        if (args.length != 2)
            System.out.println("Usage: java PercolationStats N T");
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(N, T);
        System.out.printf("%-20s = %f%n", "mean", percolationStats.mean());
        System.out.printf("%-20s = %f%n", "stddev", percolationStats.stddev());
        System.out.printf("95%% confidence interval = %f, %f%n",
                percolationStats.confidenceLo(), percolationStats.confidenceHi());

    }   // test client (described below)
}

