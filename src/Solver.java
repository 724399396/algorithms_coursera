import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by weili on 16-3-31.
 */
public class Solver {
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;

        SearchNode(Board board, SearchNode previous) {
            this.board = board;
            if (previous != null)
                this.moves = previous.moves + 1;
            else
                this.moves = 0;
        }

        @Override
        public int compareTo(SearchNode o) {
            int mahattan1 = board.manhattan();
            int mahattan2 = o.board.manhattan();
            if (mahattan1 > mahattan2) return 1;
            if (mahattan1 < mahattan2) return -1;
            return 0;
        }
    }

    private Board[] result;
    private int size = 0;
    private int moves = 0;
    private MinPQ<SearchNode> minPQ;
    private MinPQ<SearchNode> twinMinPQ;
    private boolean solved;

    public Solver(Board init) {
        if (init == null)
            throw new NullPointerException();
        minPQ = new MinPQ<>();
        twinMinPQ = new MinPQ<>();
        result = new Board[1];
        minPQ.insert(new SearchNode(init, null));
        twinMinPQ.insert(new SearchNode(init.twin(), null));
        Board curRes = init, twinRes;
        do {
            if (twinMinPQ.isEmpty()) {
                break;
            }
            SearchNode twinTmp = twinMinPQ.delMin();
            twinRes = twinTmp.board;
            if (twinRes.isGoal()) {
                break;
            }
            if (minPQ.isEmpty()) {
                break;
            }
            SearchNode curTmp = minPQ.delMin();
            curRes = curTmp.board;
            if (contains(curRes)) {
                continue;
            }
            moves = curTmp.moves;
            if (result.length >= size)
                resize(result.length + 1);
            result[size++] = curRes;
            for (Board board : curRes.neighbors()) {
                minPQ.insert(new SearchNode(board, curTmp));
            }
            for (Board board : twinRes.neighbors()) {
                twinMinPQ.insert(new SearchNode(board, twinTmp));
            }
        } while (!curRes.isGoal());
        if (curRes.isGoal()) {
            solved = true;
        } else {
            moves = -1;
            size = 0;
            result = null;
            solved = false;
        }
    }

    public boolean isSolvable() {
        return solved;
    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        return new Iterable<Board>() {
            public Iterator<Board> iterator() {
                return new Iterator<Board>() {
                    private int i = 0;
                    public boolean hasNext() {
                        return i < size;
                    }
                    public Board next() {
                        if (i >= size)
                            throw new NoSuchElementException();
                        return result[i++];
                    }
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    private void resize(int newsize) {
        Board[] tmp = new Board[newsize];
        for (int i = 0; i < size; i++) {
            tmp[i] = result[i];
        }
        result = tmp;
    }

    private boolean contains(Board board) {
        for (int i = 0; i < size; i++) {
            if (result[i].equals(board)) {
                return true;
            }
        }
        return false;
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

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            int solutionSize = 0;
            for (Board board : solver.solution()) {
                StdOut.println(board);
                solutionSize += 1;
            }
            System.out.println("solution size = " + solutionSize);
        }

    }
}
