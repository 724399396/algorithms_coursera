import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private WeightedQuickUnionUF topUnion;
    private WeightedQuickUnionUF backwash;
    private boolean[] openSet;
    private int top, bottom;
    private int n;
    private boolean initialized = false;
    public Percolation(int N) {
        if (N <= 0)
            throw new IllegalArgumentException();
        top = 0;
        bottom = N * N + 1;
        this.n = N;
        topUnion = new WeightedQuickUnionUF(N*N+2);
        openSet = new boolean[N*N+1];
        backwash = new WeightedQuickUnionUF(N*N+1);
    }

    private void init() {
        for (int i = 1; i <= n; i++) {
            topUnion.union(i, top);
            topUnion.union(bottom-i, bottom);
            backwash.union(i, top);
        }
    }

    private int coordinateCal(int i, int j) {
        return (i-1) * n + j;
    }

    private void unionNeighbor(int i, int j) {
        if (i > 1 && isOpen(i-1, j)) {
            topUnion.union(coordinateCal(i, j), coordinateCal(i - 1, j));
            backwash.union(coordinateCal(i, j), coordinateCal(i - 1, j));
        }
        if (i < n && isOpen(i+1, j)) {
            topUnion.union(coordinateCal(i, j), coordinateCal(i + 1, j));
            backwash.union(coordinateCal(i, j), coordinateCal(i + 1, j));
        }
        if (j > 1 && isOpen(i, j-1)) {
            topUnion.union(coordinateCal(i, j), coordinateCal(i, j - 1));
            backwash.union(coordinateCal(i, j), coordinateCal(i, j - 1));
        }
        if (j < n && isOpen(i, j+1)) {
            topUnion.union(coordinateCal(i, j), coordinateCal(i, j + 1));
            backwash.union(coordinateCal(i, j), coordinateCal(i, j + 1));
        }
    }

    public void open(int i, int j) {
        if (i < 1 || i > n || j < 1 || j > n)
            throw new IndexOutOfBoundsException();
        if (!initialized) {
            initialized = true;
            init();
        }
        openSet[coordinateCal(i, j)] = true;
        unionNeighbor(i, j);
    }
    public boolean isOpen(int i, int j) {
        if (i < 1 || i > n || j < 1 || j > n)
            throw new IndexOutOfBoundsException();
        return openSet[coordinateCal(i, j)];
    }
    public boolean isFull(int i, int j) {
        if (i < 1 || i > n || j < 1 || j > n)
            throw new IndexOutOfBoundsException();
        return topUnion.connected(top, coordinateCal(i, j)) && openSet[coordinateCal(i, j)]
                && backwash.connected(coordinateCal(i, j), top);
    }
    public boolean percolates() {
        return topUnion.connected(top, bottom);
    }
}
