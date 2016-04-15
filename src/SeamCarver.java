import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.Iterator;

/**
 * Created by weili on 16-4-14.
 */
public class SeamCarver {
    private DijkstraSP verticalSP;
    private DijkstraSP horizontalSP;
    private Color[][] colors;
    private int width;
    private int height;

    public SeamCarver(Picture picture) {
        checkArgNull(picture);
        width = picture.width();
        height = picture.height();
        colors = new Color[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                colors[i][j] = picture.get(i, j);

    }

    public Picture picture() {
        Picture picture = new Picture(width(), height());
        for (int i = 0; i < picture.width(); i++)
            for (int j = 0; j < picture.height(); j++)
                picture.set(i, j, colors[i][j]);
        return picture;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double energy(int x, int y) {
        if (invalidCoordinate(x, y))
            throw new IndexOutOfBoundsException();
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            return 1000.0;

        Color x2Color = colors[x+1][y];
        Color x1Color = colors[x-1][y];
        double rx = x2Color.getRed() - x1Color.getRed();
        double gx = x2Color.getGreen() - x1Color.getGreen();
        double bx = x2Color.getBlue() - x1Color.getBlue();

        Color y2Color = colors[x][y+1];
        Color y1Color = colors[x][y-1];
        double ry = y2Color.getRed() - y1Color.getRed();
        double gy = y2Color.getGreen() - y1Color.getGreen();
        double by = y2Color.getBlue() - y1Color.getBlue();

        return Math.sqrt(Math.pow(rx, 2) + Math.pow(gx, 2) + Math.pow(bx, 2)
                       + Math.pow(ry, 2) + Math.pow(gy, 2) + Math.pow(by, 2));
    }

    public int[] findHorizontalSeam() {
        // horizontal SP
        int height = height();
        int width = width();
        int top = height() * width();
        int bottom = height() * width() + 1;
        EdgeWeightedDigraph horizontalGraph = new EdgeWeightedDigraph(height * width + 2);
        for (int j = 0; j < height; j++) {
            horizontalGraph.addEdge(new DirectedEdge(top, j * width, energy(0, j)));
            horizontalGraph.addEdge(new DirectedEdge(j*width + width - 1, bottom, 0));
        }
        for (int j = 0; j < height; j++) {
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

        Iterator<DirectedEdge> paths = horizontalSP.pathTo(bottom).iterator();
        int[] res = new int[width()];
        for (int i = 0; i < width(); i++) {
            DirectedEdge edge = paths.next();
            res[i] = edge.to() / width();
        }
        return res;
    }

    public int[] findVerticalSeam() {
        // vertical SP
        int height = height();
        int width = width();
        int top = height() * width();
        int bottom = height() * width() + 1;
        EdgeWeightedDigraph verticalGraph = new EdgeWeightedDigraph(height * width + 2);
        for (int i = 0; i < width; i++) {
            verticalGraph.addEdge(new DirectedEdge(top, i, energy(i, 0)));
            verticalGraph.addEdge(new DirectedEdge(top - 1 - i, bottom, 0));
        }
        for (int j = 0; j < height - 1; j++) {
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
        if (seam.length != width() || height() <= 1)
            throw new IllegalArgumentException();
        for (int i = 0; i < width(); i++) {
            if (invalidCoordinate(i, seam[i]))
                throw new IllegalArgumentException();
            if (i > 0)
                if (Math.abs(seam[i] - seam[i-1]) > 1)
                    throw new IllegalArgumentException();
        }
        Color[][] newColors = new Color[width()][height()-1];
        for (int i = 0; i < width(); i++)
            for (int j = 0; j < height()-1; j++) {
                if (j >= seam[i])
                    newColors[i][j] = colors[i][j+1];
                else
                    newColors[i][j] = colors[i][j];
            }
        colors = newColors;
        height -= 1;
    }

    public void removeVerticalSeam(int[] seam) {
        checkArgNull(seam);
        if (seam.length != height() || width() <= 1)
            throw new IllegalArgumentException();
        for (int i = 0; i < height(); i++) {
            if (invalidCoordinate(seam[i], i))
                throw new IllegalArgumentException();
            if (i > 0)
                if (Math.abs(seam[i] - seam[i-1]) > 1)
                    throw new IllegalArgumentException();
        }
        Color[][] newColors = new Color[width()-1][height()];
        for (int j = 0; j < height(); j++)
            for (int i = 0; i < width()-1; i++) {
                if (i >= seam[j])
                    newColors[i][j] = colors[i+1][j];
                else
                    newColors[i][j] = colors[i][j];
            }
        colors = newColors;
        width -= 1;
    }

    private void checkArgNull(Object... args) {
        for (Object arg : args) {
            if (arg == null)
                throw new NullPointerException();
        }
    }

    private boolean invalidCoordinate(int x, int y) {
        return (x > width() - 1 || x < 0
                || y > height() - 1 || y < 0);
    }
}

