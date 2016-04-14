import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;

import java.awt.*;
import java.util.Iterator;

/**
 * Created by weili on 16-4-14.
 */
public class SeamCarver {
    private Picture picture;
    private DijkstraSP verticalSP;
    private DijkstraSP horizontalSP;
    private int top;
    private int bottom;

    public SeamCarver(Picture picture) {
        checkArgNull(picture);
        this.picture = picture;
        int height = picture.height();
        int width = picture.width();
        top = height * width;
        bottom = height * width + 1;

        // vertical SP
        EdgeWeightedDigraph verticalGraph = new EdgeWeightedDigraph(height * width + 2);
        for (int i = 0; i < width; i++) {
            verticalGraph.addEdge(new DirectedEdge(top, i, energy(i, 0)));
            verticalGraph.addEdge(new DirectedEdge(top - 1 - i, bottom, 0));
        }
        for (int j = 0; j < height - 1; j ++) {
            for (int i = 0; i < width; i++) {
                int cur = j * width + i;
                verticalGraph.addEdge(new DirectedEdge(cur, cur + width, energy(i, j + 1)));
                if (i > 0)
                    verticalGraph.addEdge(new DirectedEdge(cur, cur + width - 1, energy(i - 1, j + 1)));
                if (i < width - 1)
                    verticalGraph.addEdge(new DirectedEdge(cur, cur + width + 1, energy(i + 1, j + 1)));
            }
        }
        verticalSP = new DijkstraSP(verticalGraph, top);

        // horizontal SP
        EdgeWeightedDigraph horizontalGraph = new EdgeWeightedDigraph(height * width + 2);
        for (int j = 0; j < height; j ++) {
            horizontalGraph.addEdge(new DirectedEdge(top, j * width, energy(0, j)));
            horizontalGraph.addEdge(new DirectedEdge(j*width + width - 1, bottom, 0));
        }
        for (int j = 0; j < height; j ++) {
            for (int i = 0; i < width-1; i++) {
                int cur = j * width + i;
                horizontalGraph.addEdge(new DirectedEdge(cur, cur + 1, energy(i + 1, j)));
                if (j > 0)
                    horizontalGraph.addEdge(new DirectedEdge(cur, cur + 1 - width, energy(i + 1, j - 1)));
                if (j < height - 1)
                    horizontalGraph.addEdge(new DirectedEdge(cur, cur + 1 + width, energy(i + 1, j + 1)));
            }
        }
        horizontalSP = new DijkstraSP(horizontalGraph, top);
    }

    public Picture picture() {
        return picture;
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(int x, int y) {
        if (invalidCordination(x,y))
            throw new IndexOutOfBoundsException();
        if (x == 0 || x == picture.width() - 1 || y == 0 || y == picture.height() - 1)
            return 1000.0;

        Color x2Color = picture.get(x+1, y);
        Color x1Color = picture.get(x-1, y);
        double rx = x2Color.getRed() - x1Color.getRed();
        double gx = x2Color.getGreen() - x1Color.getGreen();
        double bx = x2Color.getBlue() - x1Color.getBlue();

        Color y2Color = picture.get(x+1, y);
        Color y1Color = picture.get(x-1, y);
        double ry = y2Color.getRed() - y1Color.getRed();
        double gy = y2Color.getGreen() - y1Color.getGreen();
        double by = y2Color.getBlue() - y1Color.getBlue();

        return Math.sqrt(Math.pow(rx, 2) + Math.pow(gx, 2) + Math.pow(bx, 2)
                       + Math.pow(ry, 2) + Math.pow(gy, 2) + Math.pow(by, 2));
    }

    public int[] findHorizontalSeam() {
        Iterator<DirectedEdge> paths = horizontalSP.pathTo(bottom).iterator();
        int[] res = new int[width()];
        for (int i = 0; i < width(); i++) {
            DirectedEdge edge = paths.next();
            res[i] = edge.to() / width();
        }
        return res;
    }

    public int[] findVerticalSeam() {
        Iterator<DirectedEdge> paths = verticalSP.pathTo(bottom).iterator();
        int[] res = new int[height()];
        for (int i = 0; i < height(); i++) {
            DirectedEdge edge = paths.next();
            res[i] = edge.to() % width();
        }
        return res;
    }

    public void removeHorizontalSeam(int[] seam) {
        checkArgNull(seam);
        if (seam.length != picture.width() || picture.height() <= 1)
            throw new IllegalArgumentException();
        for(int i = 0; i < picture.width(); i++) {
            if (invalidCordination(i, seam[i]))
                throw new IllegalArgumentException();
        }
    }

    public void removeVerticalSeam(int[] seam) {
        checkArgNull(seam);
        if (seam.length != picture.height() || picture.width() <= 1)
            throw new IllegalArgumentException();
        for(int i = 0; i < picture.height(); i++) {
            if (invalidCordination(seam[i], i))
                throw new IllegalArgumentException();
        }
    }

    private void checkArgNull(Object... args) {
        for (Object arg : args) {
            if (arg == null)
                throw new NullPointerException();
        }
    }

    private boolean invalidCordination(int x, int y) {
        return (x > picture.width() - 1 || x < 0
                || y > picture.height() - 1 || y < 0);
    }
}

