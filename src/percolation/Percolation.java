package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by weili on 16-3-21.
 */
public class Percolation {
    private WeightedQuickUnionUF topUnion;
    private Set<Integer> openSet;
    int top, bottom;
    int N;
    public Percolation(int N) {               // create N-by-N grid, with all sites blocked
        top = 0;
        bottom = N * N + 1;
        this.N = N;
        topUnion = new WeightedQuickUnionUF(N*N+2); // N*N for block, 2 for top and bottom
        openSet = new HashSet<>();
        for(int i = 1; i <= N; i++) {
            topUnion.union(i,top);
            topUnion.union(bottom-i,bottom);
        }

    }
    private int coordinateCal(int i, int j) {
        return (i-1) * N + j;
    }

    private void unionNeighbor(int i, int j) {
        if (i > 1 && isOpen(i-1,j))
            topUnion.union(coordinateCal(i,j), coordinateCal(i-1,j));
        if (i < N && isOpen(i+1,j))
            topUnion.union(coordinateCal(i,j), coordinateCal(i+1,j));
        if (j > 1 && isOpen(i,j-1))
            topUnion.union(coordinateCal(i,j), coordinateCal(i,j-1));
        if (j < N && isOpen(i,j+1))
            topUnion.union(coordinateCal(i,j), coordinateCal(i,j+1));
    }

    public void open(int i, int j) {  // open site (row i, column j) if it is not open already
        openSet.add(coordinateCal(i,j));
        unionNeighbor(i,j);
    }
    public boolean isOpen(int i, int j) { // is site (row i, column j) open?
        return openSet.contains(coordinateCal(i,j));
    }
    public boolean isFull(int i, int j) {
        return topUnion.connected(top, coordinateCal(i,j)) && openSet.contains(coordinateCal(i,j));
    }    // is site (row i, column j) full?
    public boolean percolates() {
        return topUnion.connected(top,bottom);
    }             // does the system percolate?

    public static void main(String[] args) {

    }  // test client (optional)
}
