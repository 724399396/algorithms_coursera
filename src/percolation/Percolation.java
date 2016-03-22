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
    private int top, bottom;
    private int n;
    public Percolation(int N) {
        top = 0;
        bottom = N * N + 1;
        this.n = N;
        topUnion = new WeightedQuickUnionUF(N*N+2);
        openSet = new HashSet<>();
        for(int i = 1; i <= N; i++) {
            topUnion.union(i, top);
            topUnion.union(bottom-i, bottom);
        }

    }
    private int coordinateCal(int i, int j) {
        return (i-1) * n + j;
    }

    private void unionNeighbor(int i, int j) {
        if (i > 1 && isOpen(i-1,j))
            topUnion.union(coordinateCal(i, j), coordinateCal(i-1, j));
        if (i < n && isOpen(i+1,j))
            topUnion.union(coordinateCal(i, j), coordinateCal(i+1, j));
        if (j > 1 && isOpen(i,j-1))
            topUnion.union(coordinateCal(i, j), coordinateCal(i, j-1));
        if (j < n && isOpen(i,j+1))
            topUnion.union(coordinateCal(i, j), coordinateCal(i, j+1));
    }

    public void open(int i, int j) {
        openSet.add(coordinateCal(i, j));
        unionNeighbor(i, j);
    }
    public boolean isOpen(int i, int j) {
        return openSet.contains(coordinateCal(i, j));
    }
    public boolean isFull(int i, int j) {
        return topUnion.connected(top, coordinateCal(i, j)) && openSet.contains(coordinateCal(i,j));
    }
    public boolean percolates() {
        return topUnion.connected(top, bottom);
    }
}
