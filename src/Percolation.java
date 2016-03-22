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
        int middle = coordinateCal(i, j);
        if (i > 1 && isOpen(i-1, j)) {
            int neighbor = coordinateCal(i - 1, j);
            topUnion.union(middle, neighbor);
            backwash.union(middle, neighbor);
        }
        if (i < n && isOpen(i+1, j)) {
            int neighbor = coordinateCal(i + 1, j);
            topUnion.union(middle, neighbor);
            backwash.union(middle, neighbor);
        }
        if (j > 1 && isOpen(i, j-1)) {
            int neighbor = coordinateCal(i, j - 1);
            topUnion.union(middle, neighbor);
            backwash.union(middle, neighbor);
        }
        if (j < n && isOpen(i, j+1)) {
            int neighbor = coordinateCal(i, j + 1);
            topUnion.union(middle, neighbor);
            backwash.union(middle, neighbor);
        }
    }

    public void open(int i, int j) {
        checkBound(i,j);
        if (!initialized) {
            initialized = true;
            init();
        }
        if (isOpen(i,j)) {
            return;
        }
        openSet[coordinateCal(i, j)] = true;
        unionNeighbor(i, j);
    }
    public boolean isOpen(int i, int j) {
        checkBound(i,j);
        return openSet[coordinateCal(i, j)];
    }
    public boolean isFull(int i, int j) {
        checkBound(i,j);
        return openSet[coordinateCal(i, j)]
                && backwash.connected(coordinateCal(i, j), top);
    }
    public boolean percolates() {
        return topUnion.connected(top, bottom);
    }

    private void checkBound(int i, int j) {
        if (i < 1 || i > n || j < 1 || j > n)
            throw new IndexOutOfBoundsException();
    }
}
