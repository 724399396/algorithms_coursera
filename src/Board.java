import edu.princeton.cs.algs4.In;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by weili on 16-3-31.
 */
public class Board {
    private int[][] blocks;
    private int n;

    public Board(int[][] blocks) {
        this.n = blocks.length;
        this.blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                this.blocks[i][j] = blocks[i][j];
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] != i * n + j + 1 && blocks[i][j] != 0)
                    hamming += 1;
        return hamming;
    }

    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] != i * n + j + 1 && blocks[i][j] != 0) {
                    int row = (blocks[i][j] - 1) / n;
                    int col = (blocks[i][j] - 1) % n;
                    manhattan += (Math.abs(row - i) + Math.abs(col - j));
                }
        return manhattan;
    }

    public boolean isGoal() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] != i * n + j + 1)
                    return (i == n - 1) && (j == n - 1);
        return true;
    }

    public Board twin() {
        int[][] title = copy();
        for (int i = 1; i < n * n; i++) {
            int row = i / n;
            int col = i % n;
            int preRow = (i - 1) / n;
            int preCol = (i - 1) % n;
            if (title[row][col] != 0 && title[preRow][preCol] != 0) {
                swap(title, row, col, preRow, preCol);
                break;
            }
        }
        return new Board(title);
    }

    private void swap(int[][] arr, int x1, int y1, int x2, int y2) {
        int tmp = arr[x1][y1];
        arr[x1][y1] = arr[x2][y2];
        arr[x2][y2] = tmp;
    }

    private int[][] copy() {
        int[][] title = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                title[i][j] = blocks[i][j];
            }
        return title;
    }

    public boolean equals(Object y) {
        if (y == null)
            return false;

        if (y.getClass() != this.getClass())
            return false;

        Board that = (Board) y;

        if (blocks.length != that.blocks.length) return false;
        if (blocks.length == 0) return true;
        if (blocks[0].length != that.blocks[0].length) return false;

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] != that.blocks[i][j])
                    return false;
        return true;
    }

    public Iterable<Board> neighbors() {
        int zeroRow = -1, zeroCol = -1;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                    break;
                }
        int num = 0;
        final Board[] neighbor = new Board[4];
        if (zeroRow > 0) {
            int[][] title = copy();
            swap(title, zeroRow, zeroCol, zeroRow - 1, zeroCol);
            neighbor[num++] = new Board(title);
        }
        if (zeroRow < n-1) {
            int[][] title = copy();
            swap(title, zeroRow, zeroCol, zeroRow + 1, zeroCol);
            neighbor[num++] = new Board(title);
        }
        if (zeroCol > 0) {
            int[][] title = copy();
            swap(title, zeroRow, zeroCol, zeroRow, zeroCol - 1);
            neighbor[num++] = new Board(title);
        }
        if (zeroCol < n-1) {
            int[][] title = copy();
            swap(title, zeroRow, zeroCol, zeroRow, zeroCol + 1);
            neighbor[num++] = new Board(title);
        }

        final int neightNum = num;
        return new Iterable<Board>() {
            public Iterator<Board> iterator() {
                return new Iterator<Board>() {
                    private int i = 0;
                    public boolean hasNext() {
                        return i < neightNum;
                    }
                    public Board next() {
                        if (i >= neightNum)
                            throw new NoSuchElementException();
                        return neighbor[i++];
                    }
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        System.out.println(initial.manhattan());
    }
}
