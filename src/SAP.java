import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private Digraph G;

    public SAP(Digraph G) {
        checkArgNull(G);
        this.G = G;
    }

    public int length(int v, int w) {
        validVertex(v);
        validVertex(w);
        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(G, w);
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (vPath.hasPathTo(i) && wPath.hasPathTo(i)) {
                int length = vPath.distTo(i) + wPath.distTo(i);
                if (length < min)
                    min = length;
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    public int ancestor(int v, int w) {
        validVertex(v);
        validVertex(w);
        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(G, w);
        int min = Integer.MAX_VALUE;;
        int loc = -1;
        for (int i = 0; i < G.V(); i++) {
            if (vPath.hasPathTo(i) && wPath.hasPathTo(i)) {
                int length = vPath.distTo(i) + wPath.distTo(i);
                if (length < min) {
                    min = length;
                    loc = i;
                }
            }
        }
        return loc;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkArgNull(v, w);
        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(G, w);
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (vPath.hasPathTo(i) && wPath.hasPathTo(i)) {
                int length = vPath.distTo(i) + wPath.distTo(i);
                if (length < min)
                    min = length;
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkArgNull(v, w);
        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(G, w);
        int min = Integer.MAX_VALUE;;
        int loc = -1;
        for (int i = 0; i < G.V(); i++) {
            if (vPath.hasPathTo(i) && wPath.hasPathTo(i)) {
                int length = vPath.distTo(i) + wPath.distTo(i);
                if (length < min) {
                    min = length;
                    loc = i;
                }
            }
        }
        return loc;
    }

    private void checkArgNull(Object... input) {
        for (Object o : input) {
            if (o == null)
                throw new NullPointerException();
        }
    }

    private void validVertex(int i) {
        if (i < 0 || i > (G.V() - 1))
            throw new IndexOutOfBoundsException();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
