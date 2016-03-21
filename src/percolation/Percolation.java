package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Created by weili on 16-3-21.
 */
public class Percolation {
    private WeightedQuickUnionUF weightedQuickUnionUF;
    int top, bottom;
    int N;
    public Percolation(int N) {               // create N-by-N grid, with all sites blocked
        top = 0;
        bottom = N * N + 1;
        this.N = N;
        weightedQuickUnionUF = new WeightedQuickUnionUF(N*N+2); // N*N for block, 2 for top and bottom
        for(int i = 1; i <= N; i++) {
            //weightedQuickUnionUF.union(i,top);
            //weightedQuickUnionUF.union(bottom-i,bottom);
        }

    }
    private int coordinateCal(int i, int j) {
        return (i-1) * N + j;
    }
    public void open(int i, int j) {  // open site (row i, column j) if it is not open already
        if (i == 1)
            weightedQuickUnionUF.union(coordinateCal(i,j),top);
        else {
            if (isFull(i-1,j) || (j > 1 && isFull(i,j-1))  || (j < N && isFull(i,j+1))) {
                weightedQuickUnionUF.union(coordinateCal(i, j), top);
            } else
                weightedQuickUnionUF.union(coordinateCal(i,j),bottom);
        }
    }
    public boolean isOpen(int i, int j) { // is site (row i, column j) open?
        return weightedQuickUnionUF.connected(bottom, coordinateCal(i,j));
    }
    public boolean isFull(int i, int j) {
        return weightedQuickUnionUF.connected(top, coordinateCal(i,j));
    }    // is site (row i, column j) full?
    public boolean percolates() {
        return weightedQuickUnionUF.connected(top,bottom);
    }             // does the system percolate?

    public static void main(String[] args) {

    }  // test client (optional)
}
